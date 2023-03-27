import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/screen_arguments.dart';

class HorizontalSocietyCardWidget extends StatefulWidget {
  final Society society;
  final List<Event> eventList;
  const HorizontalSocietyCardWidget(
      {Key? key, required this.society, required this.eventList})
      : super(key: key);

  @override
  State<HorizontalSocietyCardWidget> createState() =>
      _HorizontalSocietyCardWidgetState();
}

class _HorizontalSocietyCardWidgetState
    extends State<HorizontalSocietyCardWidget> {
  @override
  Widget build(BuildContext context) {
    double cardImageWidth = MediaQuery.of(context).size.width / 2.5;
    return Container(
      width: MediaQuery.of(context).size.width - 50,
      child: GestureDetector(
        //Card view
        child: Card(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              //image
              Column(
                children: [
                  SizedBox(
                    width: cardImageWidth,
                    height: 150,
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border.all(
                          color: Colors.transparent,
                          width: 10,
                        ),
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Icon(
                        Icons.groups_outlined,
                        size: 100,
                        color: Styles.primaryColor,
                      ),
                    ),
                  ),
                ],
              ),
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    alignment: Alignment.center,
                    constraints: BoxConstraints(
                        maxWidth: MediaQuery.of(context).size.width - 200),
                    child: Text(
                      widget.society.getName(),
                      style: Styles.HCardTitle,
                      overflow: TextOverflow.visible,
                      maxLines: 3,
                      textAlign: TextAlign.center,
                    ),
                  ),
                ],
              )
            ],
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20.0),
          ),
          elevation: 0,
        ),
        onTap: () {
          List<Society> societyList = [];
          societyList.add(widget.society);
          Navigator.pushNamed(context, '/society',
              arguments: ScreenArguments(widget.eventList, societyList));
        },
      ),
    );
  }
}
