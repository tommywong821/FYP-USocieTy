import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:intl/intl.dart';

class SocietyCarouselSliderWidget extends StatefulWidget {
  final List<Society> societyList;
  const SocietyCarouselSliderWidget({super.key, required this.societyList});

  @override
  State<SocietyCarouselSliderWidget> createState() =>
      _SocietyCarouselSliderWidgetState();
}

class _SocietyCarouselSliderWidgetState
    extends State<SocietyCarouselSliderWidget> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        CarouselSlider(
            items: widget.societyList.map((i) {
              return Builder(
                builder: (BuildContext context) {
                  return Container(
                    width: MediaQuery.of(context).size.width - 100,
                    margin: EdgeInsets.symmetric(horizontal: 1),
                    child: GestureDetector(
                      //Card view
                      child: Card(
                        child: Column(
                          children: [
                            Padding(
                              padding: const EdgeInsets.all(10.0),
                              child: SizedBox(
                                width: MediaQuery.of(context).size.width - 50,
                                height: 70,
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.transparent,
                                    borderRadius: BorderRadius.circular(20),
                                  ),
                                  child: Icon(
                                    Icons.groups_outlined,
                                    size: 100,
                                    color: Styles.primaryColor,
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              alignment: Alignment.center,
                              child: Text(i.name,
                                  style: Styles.societyCarouselSliderTitle,
                                  overflow: TextOverflow.ellipsis),
                            ),
                            Divider(
                              thickness: 3,
                              indent: 70,
                              endIndent: 70,
                              height: 20,
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text(
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu.",
                                textAlign: TextAlign.center,
                                style: Styles.societyCarouselSliderDesc,
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
                          '/society',
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
}
