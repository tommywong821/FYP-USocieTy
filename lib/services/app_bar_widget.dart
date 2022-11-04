import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:intl/intl.dart';

class AppBarWidget extends StatelessWidget {
  const AppBarWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final args =
        ModalRoute.of(context)!.settings.arguments as ProfileScreenArguments;
    var now = new DateTime.now();
    var formatter = new DateFormat('EEEE, MMMM d');
    String formattedDate = formatter.format(now);
    return Container(
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 10, left: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                    child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      formattedDate,
                      style: TextStyle(
                          color: Colors.grey,
                          fontSize: 14,
                          fontWeight: FontWeight.w500),
                    ),
                    SizedBox(
                      height: 3,
                    ),
                    Text("Hi! " + args.name,
                        style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.w600,
                          fontSize: 20,
                        )),
                  ],
                )),
                Padding(
                  padding: const EdgeInsets.only(right: 8.0),
                  child: CircleAvatar(
                    child: Icon(
                      Icons.person,
                      color: Colors.black,
                    ),
                    backgroundColor: Colors.black12,
                  ),
                ),
              ],
            ),
          ),
          SizedBox(
            height: 5,
          ),
          SizedBox(
            child: Divider(
              indent: 20,
              endIndent: 20,
              thickness: 0.5,
              color: Colors.grey.withOpacity(0.5),
            ),
          )
        ],
      ),
    );
  }
}
