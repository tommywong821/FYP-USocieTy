import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';

import '../constants.dart';
import '../services/aad_oauth_service.dart';
import '../services/storage_service.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({
    Key? key,
  }) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final AadOAuthService _aadOAuthService = AadOAuthService();
  final StorageService _storageService = StorageService();

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;

    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
            width: 150,
            height: 150,
          ),
          SizedBox(
            height: 24.0,
          ),
          Text('Name: ${args.name}'),
          SizedBox(
            height: 24.0,
          ),
          Text('email: ${args.email}'),
          SizedBox(
            height: 48.0,
          ),
          ElevatedButton(
            onPressed: () {
              // logoutAction();
              logout();
            },
            child: Text('Logout'),
          )
        ],
      ),
    );
  }

  Future<void> logout() async {
    // route to profile page
    await _aadOAuthService.logout();
    await _storageService.deleteSecureData(ACCESS_TOKEN_KEY);

    //route to welcome page when logout
    routeToWelcomePage();
  }

  void routeToWelcomePage() {
    Navigator.pushNamedAndRemoveUntil(context, '/', ModalRoute.withName('/'));
  }
}
