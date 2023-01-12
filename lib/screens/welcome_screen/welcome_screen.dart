import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/auth/jwt_token.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:ngok3fyp_frontend_flutter/services/aad_oauth_service.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';
import 'package:ngok3fyp_frontend_flutter/services/storage_service.dart';

import '../../constants.dart';
import '../../model/auth/aad_profile.dart';
import '../../model/student.dart';
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
  String fullname = '';
  String nickname = '';
  String email = '';
  String enrolledSocieties = '';

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

  void routeToHomePage() {
    Navigator.pushNamedAndRemoveUntil(
      context,
      '/home',
      ModalRoute.withName('/home'),
      arguments:
          ProfileScreenArguments(fullname, nickname, email, enrolledSocieties),
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
      await loginProcess();
    } catch (e, s) {
      // showError('error on refresh token: $e - stack: $s');
      await _aadOAuthService.logout();
      setState(() {
        isBusy = false;
      });
    }
  }

  Future<void> login() async {
    setState(() {
      isBusy = true;
      errorMessage = '';
    });

    try {
      await loginProcess();
    } catch (e) {
      showError(e);
      setState(() {
        isLoggedIn = false;
        errorMessage = e.toString();
      });
    }
  }

  Future<void> loginProcess() async {
    await _aadOAuthService.login();
    var accessToken = await _aadOAuthService.getAccessToken();
    debugPrint('Logged in successfully, your access token: $accessToken');

    final AADProfile aadProfile =
        await ApiService().getUserDetails(accessToken!);
    await _storageService.writeSecureData(ACCESS_TOKEN_KEY, accessToken);

    // get cookie from backend
    final JWTToken jwtToken =
        await ApiService().signCookieFromBackend(aadProfile);
    await _storageService.writeSecureData(
        COOKIE_KEY, "token=${jwtToken.jwtToken}");

    // fetch user profile from backend
    // get itsc from AAD
    final String itsc = aadProfile.itsc;
    final Student student = await ApiService().getStudentProfile(itsc);
    await _storageService.writeSecureData(ITSC_KEY, itsc);

    setState(() {
      isLoggedIn = true;
      fullname = aadProfile.name;
      nickname = student.nickname;
      email = student.mail;
      enrolledSocieties = student.enrolledSocieties;
    });

    routeToHomePage();
  }
}
