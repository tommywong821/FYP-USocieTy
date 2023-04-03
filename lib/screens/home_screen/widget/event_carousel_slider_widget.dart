import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:intl/intl.dart';

class EventCarouselSliderWidget extends StatefulWidget {
  final List<Event> eventList;
  final bool reducedForm;
  const EventCarouselSliderWidget(
      {Key? key, required this.eventList, this.reducedForm = false})
      : super(key: key);

  @override
  _EventCarouselSliderWidgetState createState() =>
      _EventCarouselSliderWidgetState();
}

class _EventCarouselSliderWidgetState extends State<EventCarouselSliderWidget> {
  @override
  Widget build(BuildContext context) {
    return widget.reducedForm == false
        ? regularCarousel(widget.eventList)
        : reducedCarousel(widget.eventList);
  }
}

DateTime getLocalTime(String date) {
  final DateFormat defaultDateFormat = DateFormat("M/d/y H:m");
  //-8 due to default date is in HK time zone
  DateTime utcTime = defaultDateFormat.parse(date).add(Duration(hours: -8));
  //utc time add local time offset to local time
  DateTime localTime =
      utcTime.add(Duration(hours: DateTime.now().timeZoneOffset.inHours));
  return localTime;
}

Widget regularCarousel(List<Event> event) {
  final DateFormat dateFormatter = DateFormat('E , MMM d · H:mm');
  return Column(
    children: [
      CarouselSlider(
          items: event.map((i) {
            return Builder(
              builder: (BuildContext context) {
                return Container(
                  width: MediaQuery.of(context).size.width - 50,
                  margin: EdgeInsets.symmetric(horizontal: 1.0),
                  child: GestureDetector(
                    //Card view
                    child: Card(
                      child: Column(
                        children: [
                          //image
                          SizedBox(
                            width: MediaQuery.of(context).size.width - 100,
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
                                    i.poster,
                                    fit: BoxFit.fitWidth,
                                  ),
                                  borderRadius: BorderRadius.circular(20),
                                )),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(left: 15),
                            child: Container(
                              child: Text(
                                dateFormatter.format(getLocalTime(i.startDate)),
                                style: Styles.carouselSliderDate,
                              ),
                              alignment: Alignment.centerLeft,
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(left: 15, top: 5),
                            child: Container(
                              alignment: Alignment.centerLeft,
                              child: Text(i.name,
                                  style: Styles.carouselSliderTitle,
                                  overflow: TextOverflow.ellipsis),
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.only(top: 7, left: 10),
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
                                      i.location,
                                      style: Styles.carouselSliderLocation,
                                    ),
                                    alignment: Alignment.centerLeft,
                                  ),
                                ),
                              ],
                            ),
                          )
                        ],
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20.0),
                      ),
                      elevation: 0,
                      margin: EdgeInsets.all(10),
                    ),
                    onTap: () {
                      Navigator.pushNamed(
                        context,
                        '/event',
                        arguments: i,
                      );
                    },
                  ),
                );
              },
            );
          }).toList(),
          options: CarouselOptions(height: 250.0))
    ],
  );
}

Widget reducedCarousel(List<Event> event) {
  final DateFormat dateFormatter = DateFormat('E , MMM d · H:mm');
  return Column(
    children: [
      CarouselSlider(
          items: event.map((i) {
            return Builder(
              builder: (BuildContext context) {
                return Container(
                  width: MediaQuery.of(context).size.width - 100,
                  margin: EdgeInsets.symmetric(horizontal: 1.0),
                  child: GestureDetector(
                    //Card view
                    child: Card(
                      child: Column(
                        children: [
                          //image
                          SizedBox(
                            width: MediaQuery.of(context).size.width - 50,
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
                                    i.poster,
                                    fit: BoxFit.fitWidth,
                                  ),
                                  borderRadius: BorderRadius.circular(20),
                                )),
                          ),
                          //title
                          Padding(
                            padding: const EdgeInsets.only(left: 10),
                            child: Text(i.name,
                                style: Styles.carouselSliderTitle,
                                overflow: TextOverflow.ellipsis),
                          ),
                          //Date
                          Padding(
                            padding: const EdgeInsets.only(top: 10, left: 10),
                            child: Container(
                              child: Text(
                                dateFormatter.format(getLocalTime(i.startDate)),
                                style: Styles.carouselSliderDate,
                              ),
                              alignment: Alignment.centerLeft,
                            ),
                          ),
                          //Location
                          Padding(
                            padding: const EdgeInsets.only(top: 7, left: 5),
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
                                      i.location,
                                      style: Styles.carouselSliderLocation,
                                    ),
                                    alignment: Alignment.centerLeft,
                                  ),
                                ),
                              ],
                            ),
                          )
                        ],
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20.0),
                      ),
                      elevation: 0,
                      margin: EdgeInsets.all(5),
                    ),
                    onTap: () {
                      Navigator.pushNamed(
                        context,
                        '/event',
                        arguments: i,
                      );
                    },
                  ),
                );
              },
            );
          }).toList(),
          options: CarouselOptions(
            height: 250.0,
            viewportFraction: 0.5,
            aspectRatio: 0.5,
          ))
    ],
  );
}
