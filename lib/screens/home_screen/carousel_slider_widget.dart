import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:intl/intl.dart';

class CarouselSliderWidget extends StatefulWidget {
  final List<Event> event;
  final bool reducedForm;
  const CarouselSliderWidget(
      {Key? key, required this.event, this.reducedForm = false})
      : super(key: key);

  @override
  _CarouselSliderWidgetState createState() => _CarouselSliderWidgetState();
}

class _CarouselSliderWidgetState extends State<CarouselSliderWidget> {
  @override
  Widget build(BuildContext context) {
    return widget.reducedForm == false
        ? regularCarousel(widget.event)
        : reducedCarousel(widget.event);
  }
}

Widget regularCarousel(List<Event> event) {
  final DateFormat dateFormatter = DateFormat('E , MMM d · ');
  final time = '13:30 - 15:00';
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
                                //"Mon, Nov 28 · 13:30 - 15:00",
                                dateFormatter
                                        .format(DateTime.parse(i.date))
                                        .toString() +
                                    time,
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
  final DateFormat dateFormatter = DateFormat('E , MMM d · ');
  final time = '13:30 - 15:00';
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
                                dateFormatter
                                        .format(DateTime.parse(i.date))
                                        .toString() +
                                    time,
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
