import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/horizontal_event_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';

class IncomingEvnetScreen extends StatefulWidget {
  const IncomingEvnetScreen({Key? key}) : super(key: key);

  @override
  _IncomingEvnetScreenState createState() => _IncomingEvnetScreenState();
}

class _IncomingEvnetScreenState extends State<IncomingEvnetScreen> {
  @override
  Widget build(BuildContext context) {
    final List<Event> eventList =
        ModalRoute.of(context)!.settings.arguments as List<Event>;
    int eventCount = eventList.length;
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
                return HorizontalEventCardWidget(
                  event: eventList[index],
                );
              }),
            ),
          )
        ],
      ),
    ));
  }
}
