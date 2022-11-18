import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:intl/intl.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class AppBarWidget extends StatelessWidget {
  const AppBarWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;
    var now = new DateTime.now().toLocal();
    var formatter = new DateFormat('EEEE, MMMM d');
    String formattedDate = formatter.format(now);
    return Container(
      child: Column(
        children: [
          Padding(
            padding: EdgeInsets.only(top: 10, left: 15),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                    child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      formattedDate,
                      style: Styles.appBarDate,
                    ),
                    SizedBox(
                      height: 3,
                    ),
                    Text("Hi! " + args.name, style: Styles.appBarName),
                  ],
                )),
                Padding(
                    padding: EdgeInsets.only(right: 15),
                    child: Container(
                      height: 50,
                      width: 50,
                      decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(10),
                          image: DecorationImage(
                              fit: BoxFit.cover,
                              image: AssetImage("assets/images/avatar.png"))),
                    )),
              ],
            ),
          ),
          SizedBox(
            height: 10,
          ),
          // SizedBox(
          //   child: Divider(
          //     indent: 20,
          //     endIndent: 20,
          //     thickness: 0.5,
          //     color: Colors.grey.withOpacity(0.5),
          //   ),
          // )
        ],
      ),
    );
  }
}
