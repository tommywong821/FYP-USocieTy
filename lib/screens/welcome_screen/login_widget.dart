import 'package:flutter/material.dart';

class LoginWidget extends StatelessWidget {
  final loginAction;
  final String loginError;

  const LoginWidget(this.loginAction, this.loginError, {super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Image.asset('assets/images/red-bird-sundial.png'),
        const Text(
          'Read for your next adventure ?',
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
        ),
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
