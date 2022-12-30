import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:ngok3fyp_frontend_flutter/model/data.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

/* ***WILL DELETE IT SOON, will combine it with carousel_slider_widget.dart with parameters*** */

class CarouselSliderWidget extends StatefulWidget {
  const CarouselSliderWidget({Key? key}) : super(key: key);

  @override
  _CarouselSliderWidgetState createState() => _CarouselSliderWidgetState();
}

class _CarouselSliderWidgetState extends State<CarouselSliderWidget> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        CarouselSlider(
            items: data.map((i) {
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
                                      i["image"],
                                      fit: BoxFit.fitWidth,
                                    ),
                                    borderRadius: BorderRadius.circular(20),
                                  )),
                            ),
                            //title
                            Padding(
                              padding: const EdgeInsets.only(left: 10),
                              child: Text(i["title"],
                                  style: Styles.carouselSliderTitle,
                                  overflow: TextOverflow.ellipsis),
                            ),
                            //Date
                            Padding(
                              padding: const EdgeInsets.only(top: 10, left: 10),
                              child: Container(
                                child: Text(
                                  "Mon, Nov 28 · 13:30 - 15:00",
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
                                        "HKUST Art Hall",
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
                          arguments: i["eventID"],
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
}
