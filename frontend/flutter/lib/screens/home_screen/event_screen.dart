import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';
import 'package:intl/intl.dart';
import 'package:quickalert/quickalert.dart';

class EventScreen extends StatefulWidget {
  const EventScreen({
    Key? key,
  }) : super(key: key);

  @override
  _EventScreenState createState() => _EventScreenState();
}

class _EventScreenState extends State<EventScreen> {
  DateTime getLocalTime(String date) {
    final DateFormat defaultDateFormat = DateFormat("dd/M/y H:m");
    //-8 due to default date is in HK time zone
    DateTime utcTime = defaultDateFormat.parse(date).add(Duration(hours: -8));
    //utc time add local time offset to local time
    DateTime localTime =
        utcTime.add(Duration(hours: DateTime.now().timeZoneOffset.inHours));
    return localTime;
  }

  String getGMT() {
    String offset = DateTime.now().timeZoneOffset.inHours.toString();
    //return GMT-i or GMT+i, i is offset
    return (offset.startsWith("-")) ? " GMT" + offset : " GMT+" + offset;
  }

  @override
  Widget build(BuildContext context) {
    final DateFormat dateFormatter = DateFormat('MMM d');
    final DateFormat timeFormat = DateFormat("H:mm");

    final Event event = ModalRoute.of(context)!.settings.arguments as Event;

    String eventImage = event.poster;
    String eventTitle = event.name;
    String eventContent = event.description;
    String eventLocation = event.location;
    String eventStartDate = event.startDate;
    String eventEndDate = event.endDate;
    String eventDeadline = event.applyDeadline;
    String eventSociety = event.society;
    String eventSocietyHoldingEventNumber =
        event.societyHoldingEventNumber.toString();
    String eventFee = event.fee.toString();
    return Scaffold(
        bottomNavigationBar: BottomRegisterButton(context, event),
        body: Container(
          width: double.maxFinite,
          height: double.maxFinite,
          child: Stack(children: [
            //Image
            Positioned(
                left: 0,
                right: 0,
                child: Container(
                  width: double.maxFinite,
                  height: 350,
                  decoration: BoxDecoration(
                      image: DecorationImage(
                          image: NetworkImage(eventImage), fit: BoxFit.cover)),
                )),
            //gradient effect for the image decoration (avoid white image overlapping the white arrow button)
            Container(
              height: 350,
              decoration: BoxDecoration(
                  color: Colors.white,
                  gradient: LinearGradient(
                      begin: FractionalOffset.bottomCenter,
                      end: FractionalOffset.topCenter,
                      colors: [
                        Colors.grey.withOpacity(0.0),
                        Colors.black,
                      ],
                      stops: [
                        0.5,
                        1.0
                      ])),
            ),
            //Top left arrow button
            Positioned(
                left: 10,
                top: 30,
                child: Row(
                  children: [
                    IconButton(
                      onPressed: (() => {Navigator.pop(context)}),
                      icon: Icon(Icons.arrow_back),
                      color: Colors.white,
                    ),
                  ],
                )),
            //Content
            Positioned(
              //top, bot, left, right is required (even is 0) for SingleChildScrollView in Positioned
              top: 350,
              bottom: 0,
              left: 0,
              right: 0,
              child: Container(
                  width: MediaQuery.of(context).size.width,
                  height: double.maxFinite,
                  decoration: BoxDecoration(
                    color: Colors.white,
                  ),
                  child: SingleChildScrollView(
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          //Title
                          Padding(
                            padding: const EdgeInsets.only(top: 10, left: 20),
                            child: Container(
                              child: Container(
                                child: Text(
                                  eventTitle,
                                  style: Styles.eventScreenTitle,
                                  maxLines: 2,
                                ),
                              ),
                              alignment: Alignment.centerLeft,
                            ),
                          ),
                          //Society icon
                          Padding(
                            padding: const EdgeInsets.only(top: 20, left: 30),
                            child: Container(
                              alignment: Alignment.centerLeft,
                              child: Row(
                                children: [
                                  CircleAvatar(
                                    child: Icon(
                                      Icons.groups,
                                      color: Colors.white,
                                    ),
                                    backgroundColor: Styles.primaryColor,
                                  ),
                                  Flexible(
                                    child: Column(
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        //Society name
                                        Padding(
                                          padding: const EdgeInsets.only(
                                            left: 15,
                                          ),
                                          child: Text(
                                            eventSociety,
                                            style: Styles.eventScreenBlackText,
                                          ),
                                        ),
                                        //Society events count
                                        Padding(
                                          padding:
                                              const EdgeInsets.only(left: 15),
                                          child: Text(
                                            "$eventSocietyHoldingEventNumber Upcoming Events",
                                            style: Styles.eventScreenGreyText,
                                          ),
                                        ),
                                        //Society events count
                                      ],
                                    ),
                                  )
                                ],
                              ),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(top: 10, bottom: 10),
                            child: Divider(
                              indent: 20,
                              endIndent: 20,
                              thickness: 0.5,
                              color: Colors.grey.withOpacity(0.5),
                            ),
                          ),
                          //event start date
                          Padding(
                            padding: const EdgeInsets.only(left: 20),
                            child: Row(
                              children: [
                                Icon(Icons.calendar_month),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "From date",
                                        style: Styles.eventScreenGreyText,
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text(
                                          dateFormatter.format(getLocalTime(
                                                  eventStartDate)) +
                                              ", " +
                                              timeFormat.format(getLocalTime(
                                                  eventStartDate)) +
                                              getGMT(),
                                          style: Styles.eventScreenBlackText),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          //event end date
                          Padding(
                            padding: const EdgeInsets.only(left: 20, top: 20),
                            child: Row(
                              children: [
                                Icon(Icons.calendar_month),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "To date",
                                        style: Styles.eventScreenGreyText,
                                      ),
                                    ),
                                    //Event date
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text(
                                          dateFormatter.format(
                                                  getLocalTime(eventEndDate)) +
                                              ", " +
                                              timeFormat.format(
                                                  getLocalTime(eventEndDate)) +
                                              getGMT(),
                                          style: Styles.eventScreenBlackText),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          //Location
                          Padding(
                            padding: const EdgeInsets.only(top: 20, left: 20),
                            child: Row(
                              children: [
                                Icon(Icons.location_pin),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    //Location
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text(eventLocation,
                                          style: Styles.eventScreenBlackText),
                                    ),
                                    //Adrress
                                  ],
                                ),
                              ],
                            ),
                          ),
                          //Price
                          Padding(
                            padding: const EdgeInsets.only(top: 20, left: 20),
                            child: Row(
                              children: [
                                Icon(Icons.attach_money),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    //Price
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text(eventFee + " per Member",
                                          style: Styles.eventScreenBlackText),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.only(left: 20, top: 20),
                            child: Row(
                              children: [
                                Icon(Icons.event_busy),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "Apply deadline",
                                        style: Styles.eventScreenGreyText,
                                      ),
                                    ),
                                    //Event date
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text(
                                          dateFormatter.format(
                                                  getLocalTime(eventDeadline)) +
                                              ", " +
                                              timeFormat.format(
                                                  getLocalTime(eventDeadline)) +
                                              getGMT(),
                                          style: Styles.eventScreenBlackText),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.only(top: 10),
                            child: Divider(
                              indent: 20,
                              endIndent: 20,
                              thickness: 0.5,
                              color: Colors.grey.withOpacity(0.5),
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.only(top: 10, left: 20),
                            child: Text("Description",
                                style: Styles.eventScreenBlackText),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(
                                top: 20, left: 20, bottom: 20),
                            child: Text(eventContent,
                                style: Styles.eventScreenText),
                          ),
                        ]),
                  )),
            ),
          ]),
        ));
  }
}

Widget BottomRegisterButton(BuildContext context, Event event) {
  String eventName = event.name;
  Color hkustColor = Color.fromARGB(255, 0, 51, 102);
  return Material(
      color: Colors.white,
      child: Container(
        child: SizedBox(
          height: kBottomNavigationBarHeight + 20,
          width: double.infinity,
          child: Center(
              child: ElevatedButton(
            child: Text("Register"),
            onPressed: () {
              QuickAlert.show(
                context: context,
                type: QuickAlertType.confirm,
                title: "Do you want to register for " + eventName + "?",
                confirmBtnColor: Styles.primaryColor,
                confirmBtnText: "Yes",
                cancelBtnText: "No",
                onConfirmBtnTap: () async {
                  Navigator.pop(context);
                  QuickAlert.show(
                      context: context,
                      type: QuickAlertType.loading,
                      text: "",
                      title: "Loading");
                  bool response = await ApiService().registerEvent(event.id);
                  if (response) {
                    Navigator.pop(context);
                    QuickAlert.show(
                        context: context,
                        type: QuickAlertType.success,
                        title: "SUCCESS",
                        text: "Please wait for the approval",
                        confirmBtnText: "OK",
                        confirmBtnColor: Styles.primaryColor);
                  } else {
                    Navigator.pop(context);
                    QuickAlert.show(
                        context: context,
                        type: QuickAlertType.error,
                        title: "ERROR",
                        text: "Please contact society for assistance",
                        confirmBtnText: "OK",
                        confirmBtnColor: Styles.primaryColor);
                  }
                },
              );
            },
            style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(25),
                ),
                minimumSize: Size(350, kToolbarHeight - 10),
                backgroundColor: hkustColor),
          )),
        ),
        decoration: BoxDecoration(
            border:
                Border.all(color: Colors.grey.withOpacity(0.5), width: 0.3)),
      ));
}
