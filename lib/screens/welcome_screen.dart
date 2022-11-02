import 'dart:convert';

import 'package:aad_oauth/aad_oauth.dart';
import 'package:aad_oauth/model/config.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import '../constants.dart';
import 'package:ngok3fyp_frontend_flutter/services/storage_service.dart';

import '../Profile.dart';
import '../main.dart';
import 'login_button.dart';

class WelcomeScreen extends StatefulWidget {
  const WelcomeScreen({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  State<WelcomeScreen> createState() => _WelcomeScreenState();
}

class _WelcomeScreenState extends State<WelcomeScreen> {
  final StorageService _storageService = StorageService();

  static final Config config = Config(
      tenant: dotenv.env['TENANT']!,
      clientId: dotenv.env['CLIENTID']!,
      redirectUri: dotenv.env['REDIRECTURI']!,
      scope: dotenv.env['SCOPE']!,
      clientSecret: dotenv.env['CLIENTSECRET']!,
      navigatorKey: navigatorKey);
  final AadOAuth oAuth = AadOAuth(config);

  bool isBusy = false;
  String errorMessage = '';
  bool isLoggedIn = false;
  String name = '';
  String email = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
      child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset('assets/images/red-bird-sundial.png'),
            const Text(
              'Read for your next adventure ?',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
            isBusy
                ? const CircularProgressIndicator()
                : isLoggedIn
                    ? Profile(logout, name, email) //todo change to home page widget
                    : LoginButton(login, errorMessage),
          ]),
    ));
  }

  Future<void> login() async {
    setState(() {
      isBusy = true;
      errorMessage = '';
    });

    try {
      await oAuth.login();
      var accessToken = await oAuth.getAccessToken();
      debugPrint('Logged in successfully, your access token: $accessToken');

      final profile = await getUserDetails(accessToken!);

      await _storageService.writeSecureData(ACCESS_TOKEN_KEY, accessToken);

      setState(() {
        isBusy = false;
        isLoggedIn = true;
        name = profile['name'];
        email = profile['email'];
      });
    } catch (e) {
      showError(e);
      setState(() {
        isBusy = false;
        isLoggedIn = false;
        errorMessage = e.toString();
      });
    }
  }

  void showMessage(String text) {
    var alert = AlertDialog(
      content: Text(text),
      actions: <Widget>[
        TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: const Text('Ok'))
      ],
    );
    showDialog(context: context, builder: (BuildContext context) => alert);
  }

  void showError(dynamic ex) {
    showMessage(ex.toString());
  }

  void logout() async {
    await oAuth.logout();
    await _storageService.deleteSecureData(ACCESS_TOKEN_KEY);
    debugPrint('Logged out');
    setState(() {
      isLoggedIn = false;
      isBusy = false;
    });
  }

  Future<Map<String, dynamic>> getUserDetails(String accessToken) async {
    final url = Uri.https('graph.microsoft.com', 'oidc/userinfo');
    final response = await http.get(
      url,
      headers: {'Authorization': 'Bearer $accessToken'},
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Failed to get user details');
    }
  }

  @override
  void initState() {
    initAction();
    super.initState();
  }

  void initAction() async {
    final storedRefreshToken =
        await _storageService.readSecureData(ACCESS_TOKEN_KEY);
    if (storedRefreshToken == null) return;

    setState(() {
      isBusy = true;
    });

    try {
      final profile = await getUserDetails(storedRefreshToken);

      _storageService.writeSecureData('access_token', storedRefreshToken);

      setState(() {
        isBusy = false;
        isLoggedIn = true;
        name = profile['name'];
        email = profile['email'];
      });
    } catch (e, s) {
      showError('error on refresh token: $e - stack: $s');
      logout();
    }
  }
}
