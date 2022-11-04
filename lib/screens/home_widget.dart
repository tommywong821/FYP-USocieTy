import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:ngok3fyp_frontend_flutter/services/app_bar_widget.dart';

//for testing widget & will remove later
class HomeWidget extends StatelessWidget {
  const HomeWidget({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;
    return Scaffold(
      backgroundColor: Colors.white,
      body: Column(
          children: [AppBarWidget(), Container(child: Text("card view"))]),
    );
  }
}
