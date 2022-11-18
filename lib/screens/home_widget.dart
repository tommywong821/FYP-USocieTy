import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/services/app_bar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/tab_bar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/carousel_slider_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class HomeWidget extends StatefulWidget {
  const HomeWidget({
    Key? key,
  }) : super(key: key);
  @override
  State<HomeWidget> createState() => _HomeWidget();
}

class _HomeWidget extends State<HomeWidget> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Styles.backGroundColor,
      body: Column(children: [
        //App Bar
        AppBarWidget(),
        Expanded(
            child: SingleChildScrollView(
                child: Column(
          children: [
            //Feature Title
            Padding(
              padding: const EdgeInsets.only(left: 15),
              child: Container(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Featured",
                  style: Styles.carouselTitle,
                ),
              ),
            ),
            //TODO hero animation for carousel
            //Tab Bar with Carousel slider
            TabBarWidget(),
            //Incoming Title
            Padding(
              padding: const EdgeInsets.only(left: 15, top: 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Container(
                    alignment: Alignment.centerLeft,
                    child: Text("Incoming", style: Styles.carouselTitle),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(right: 15),
                    child: GestureDetector(
                      onTap: () {
                        print("see all");
                      },
                      child: Container(
                        child: Text(
                          "See All",
                          style: Styles.seeAll,
                        ),
                      ),
                    ),
                  )
                ],
              ),
            ),
            //Incoming Carousel
            Padding(
              padding: const EdgeInsets.only(top: 10),
              child: CarouselSliderWidget(),
            ),
            //Society Title
            Padding(
              padding: const EdgeInsets.only(left: 15, top: 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Container(
                    child: Text(
                      "Society",
                      style: Styles.carouselTitle,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(right: 15),
                    child: GestureDetector(
                      onTap: () {
                        print("see all");
                      },
                      child: Container(
                        child: Text(
                          "See All",
                          style: Styles.seeAll,
                        ),
                      ),
                    ),
                  )
                ],
              ),
            ),
            //Society Carousel
            Padding(
              padding: const EdgeInsets.only(top: 10),
              child: CarouselSliderWidget(),
            ),
          ],
        ))),
      ]),
    );
  }
}
