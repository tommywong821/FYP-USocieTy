import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/services/navigation_bar.dart';

class DummyPage extends StatefulWidget {
  final logoutAction;
  final String name;
  final String email;

  const DummyPage(this.logoutAction, this.name, this.email);

  @override
  _DummyPageState createState() => _DummyPageState();
}

class _DummyPageState extends State<DummyPage> {
  @override
  Widget build(BuildContext context) {
    return Container();
  }

  @override
  void initState() {
    Navigator.pushReplacement(
        context,
        MaterialPageRoute(
            builder: (context) =>
                HomePage(widget.logoutAction, widget.name, widget.email)));
    super.initState();
  }
}
