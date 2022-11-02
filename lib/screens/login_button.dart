import 'package:flutter/material.dart';

class LoginButton extends StatelessWidget {
  final loginAction;
  final String loginError;

  const LoginButton(this.loginAction, this.loginError, {super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        ElevatedButton(
          onPressed: () {
            loginAction();
          },
          child: const Text('Login via your itsc account'),
        ),
        Text(loginError)
      ],
    );
  }
}
