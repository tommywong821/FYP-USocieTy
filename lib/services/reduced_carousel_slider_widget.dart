import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:ngok3fyp_frontend_flutter/model/data.dart';

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
                            Text(i["title"]),
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
