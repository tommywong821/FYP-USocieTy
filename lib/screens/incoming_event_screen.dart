import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/calendar_screen/horizontal_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class IncomingEvnetScreen extends StatefulWidget {
  const IncomingEvnetScreen({Key? key}) : super(key: key);

  @override
  _IncomingEvnetScreenState createState() => _IncomingEvnetScreenState();
}

class _IncomingEvnetScreenState extends State<IncomingEvnetScreen> {
  int eventCount = 6;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: SafeArea(
      child: Column(
        children: [
          Row(
            children: [
              IconButton(
                onPressed: (() => {Navigator.pop(context)}),
                icon: Icon(Icons.arrow_back),
                color: Colors.black,
              ),
              Text(
                "Incoming Events",
                style: Styles.incEventTitle,
              )
            ],
          ),
          Expanded(
            child: ListView.separated(
              padding: const EdgeInsets.only(left: 5, right: 5),
              itemCount: eventCount,
              separatorBuilder: ((context, index) {
                return const SizedBox(height: 1);
              }),
              itemBuilder: ((context, index) {
                return HorizontalCardWidget(
                  eventID: index,
                );
              }),
            ),
          )
        ],
      ),
    ));
  }
}
