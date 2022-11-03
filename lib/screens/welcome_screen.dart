import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:ngok3fyp_frontend_flutter/services/aad_oauth_service.dart';
import '../constants.dart';
import 'package:ngok3fyp_frontend_flutter/services/storage_service.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';

import 'login_widget.dart';

class WelcomeScreen extends StatefulWidget {
  const WelcomeScreen({Key? key}) : super(key: key);

  @override
  State<WelcomeScreen> createState() => _WelcomeScreenState();
}

class _WelcomeScreenState extends State<WelcomeScreen> {
  final StorageService _storageService = StorageService();
  final AadOAuthService _aadOAuthService = AadOAuthService();

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
            isBusy
                ? const CircularProgressIndicator()
                : LoginWidget(login, errorMessage),
          ]),
    ));
  }

  Future<void> login() async {
    setState(() {
      isBusy = true;
      errorMessage = '';
    });

    try {
      await _aadOAuthService.login();
      var accessToken = await _aadOAuthService.getAccessToken();
      debugPrint('Logged in successfully, your access token: $accessToken');

      final profile = await ApiService().getUserDetails(accessToken!);

      await _storageService.writeSecureData(ACCESS_TOKEN_KEY, accessToken);

      setState(() {
        isLoggedIn = true;
        name = profile['name'];
        email = profile['email'];
      });

      routeToHomePage();
    } catch (e) {
      showError(e);
      setState(() {
        isLoggedIn = false;
        errorMessage = e.toString();
      });
    }
  }

  void routeToHomePage() {
    Navigator.pushNamedAndRemoveUntil(
      context,
      '/home',
      ModalRoute.withName('/home'),
      arguments: ProfileScreenArguments(
          name, email), //todo see if any arguments need to pass
    );
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
      final profile = await ApiService().getUserDetails(storedRefreshToken);

      _storageService.writeSecureData('access_token', storedRefreshToken);

      setState(() {
        isBusy = false;
        isLoggedIn = true;
        name = profile['name'];
        email = profile['email'];
      });
    } catch (e, s) {
      showError('error on refresh token: $e - stack: $s');
      await _aadOAuthService.logout();
      setState(() {
        isLoggedIn = false;
        isBusy = false;
      });
    }
  }
}
