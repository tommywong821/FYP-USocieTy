import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/horizontal_society_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/screen_arguments.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';

class AllSocietyScreen extends StatefulWidget {
  const AllSocietyScreen({super.key});

  @override
  State<AllSocietyScreen> createState() => _AllSocietyScreenState();
}

class _AllSocietyScreenState extends State<AllSocietyScreen> {
  @override
  Widget build(BuildContext context) {
    final ScreenArguments screenArguments =
        ModalRoute.of(context)!.settings.arguments as ScreenArguments;
    final List<Society> societyList = screenArguments.societyList;
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
                "All Societies",
                style: Styles.incEventTitle,
              )
            ],
          ),
          Expanded(
            child: ListView.separated(
              padding: const EdgeInsets.only(left: 5, right: 5),
              itemCount: societyList.length,
              separatorBuilder: ((context, index) {
                return const SizedBox(height: 1);
              }),
              itemBuilder: ((context, index) {
                return HorizontalSocietyCardWidget(
                  society: societyList[index],
                  eventList: screenArguments.enrolledEventList,
                );
              }),
            ),
          ),
        ],
      )),
    );
  }
}
