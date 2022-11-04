import 'package:flutter/material.dart';

class EventScreen extends StatefulWidget {
  const EventScreen({
    Key? key,
  }) : super(key: key);

  @override
  _EventScreenState createState() => _EventScreenState();
}

class _EventScreenState extends State<EventScreen> {
  @override
  Widget build(BuildContext context) {
    final eventID = ModalRoute.of(context)!.settings.arguments.toString();
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.only(top: 300),
          child: Column(
            children: [
              Text(eventID),
              ElevatedButton(
                child: Text("Pop"),
                onPressed: () => {Navigator.pop(context)},
              )
            ],
          ),
        ),
      ),
    );
  }
}
