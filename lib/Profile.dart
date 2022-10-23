import 'package:flutter/material.dart';

class Profile extends StatelessWidget {
  final logoutAction;
  final String name;
  final String email;

  const Profile(this.logoutAction, this.name, this.email);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Container(
          width: 150,
          height: 150,
        ),
        SizedBox(
          height: 24.0,
        ),
        Text('Name: $name'),
        SizedBox(
          height: 24.0,
        ),
        Text('email: $email'),
        SizedBox(
          height: 48.0,
        ),
        ElevatedButton(
          onPressed: () {
            logoutAction();
          },
          child: Text('Logout'),
        )
      ],
    );
  }
}
