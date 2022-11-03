import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';

//for testing widget & will remove later
class HomeWidget extends StatelessWidget {
  const HomeWidget({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;
    return Center(
        child: Container(
      width: MediaQuery.of(context).size.width / 1.4,
      height: 50,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10.0),
        color: Color(0xff9450e7),
      ),
      child: MaterialButton(
        onPressed: () {},
        child: Text("card view.."),
      ),
    ));
  }
}
