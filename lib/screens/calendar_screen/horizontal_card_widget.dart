import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/data.dart';
import 'package:ngok3fyp_frontend_flutter/model/event/event.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class HorizontalCardWidget extends StatefulWidget {
  final int eventID;
  final Event? eventObj;
  const HorizontalCardWidget({Key? key, required this.eventID, this.eventObj})
      : super(key: key);

  @override
  _HorizontalCardWidgetState createState() => _HorizontalCardWidgetState();
}

class _HorizontalCardWidgetState extends State<HorizontalCardWidget> {
  @override
  Widget build(BuildContext context) {
    double cardImageWidth = MediaQuery.of(context).size.width / 2.5;
    int eventID = widget.eventID;
    Event? eventObj = widget.eventObj ?? null;
    // print("eventID: $eventID");
    // print("eventObj: $eventObj");

    return Container(
      width: MediaQuery.of(context).size.width - 50,
      child: GestureDetector(
        //Card view
        child: Card(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              //image
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
                    child: ClipRRect(
                      child: Image.network(
                        eventObj != null
                            ? eventObj.poster
                            : data[eventID]["image"],
                        fit: BoxFit.fitWidth,
                      ),
                      borderRadius: BorderRadius.circular(20),
                    )),
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 25),
                    child: Container(
                      width: MediaQuery.of(context).size.width -
                          (cardImageWidth + 20),
                      alignment: Alignment.centerLeft,
                      child: Row(
                        children: <Widget>[
                          Flexible(
                            child: Text(
                              eventObj != null
                                  ? eventObj.name
                                  : data[eventID]["title"],
                              style: Styles.HCardTitle,
                              overflow: TextOverflow.ellipsis,
                              maxLines: 1,
                            ),
                          ),
                          Spacer(),
                          Text(eventObj != null
                              ? eventObj.category
                              : "null category")
                        ],
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 15),
                    child: Container(
                      child: Text("date"),
                      alignment: Alignment.centerLeft,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 15),
                    child: Row(
                      children: [
                        Icon(
                          Icons.location_pin,
                          color: Styles.primaryColor,
                        ),
                        Padding(
                          padding: const EdgeInsets.only(left: 5),
                          child: Container(
                            child: Text(
                              eventObj != null
                                  ? eventObj.location
                                  : "HKUST Art Hall",
                              style: Styles.HCardLocation,
                            ),
                            alignment: Alignment.centerLeft,
                          ),
                        ),
                      ],
                    ),
                  )
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
          // Navigator.pushNamed(
          //   context,
          //   '/****',
          //   arguments: eventID
          // );
        },
      ),
    );
  }
}
