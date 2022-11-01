import 'dart:convert';
import 'dart:developer';
import 'home.dart';

import 'package:aad_oauth/aad_oauth.dart';
import 'package:aad_oauth/model/config.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:ngok3fyp_frontend_flutter/Login.dart';
import 'package:ngok3fyp_frontend_flutter/Profile.dart';
import 'package:ngok3fyp_frontend_flutter/home.dart';

// void main() => runApp(MyApp());
void main() async {
  await dotenv.load(fileName: '.env');
  runApp(MyApp());
}

final navigatorKey = GlobalKey<NavigatorState>();
final FlutterSecureStorage secureStorage = const FlutterSecureStorage();

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AAD OAuth Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      // home: MyHomePage(title: 'AAD OAuth Home'),
      // changed to homePage for testing
      home: homePage(),
      navigatorKey: navigatorKey,
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
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
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: Center(
          child: isBusy
              ? CircularProgressIndicator()
              : isLoggedIn
                  ? Profile(logout, name, email)
                  : Login(login, errorMessage),
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

      await secureStorage.write(key: 'access_token', value: accessToken);

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
    await secureStorage.delete(key: 'access_token');
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
    // TODO: implement initState
    initAction();
    super.initState();
  }

  void initAction() async {
    final storedRefreshToken = await secureStorage.read(key: 'access_token');
    if (storedRefreshToken == null) return;

    setState(() {
      isBusy = true;
    });

    try {
      final profile = await getUserDetails(storedRefreshToken);

      secureStorage.write(key: 'access_token', value: storedRefreshToken);

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
