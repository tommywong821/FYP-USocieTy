import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/services/app_bar_widget.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:ngok3fyp_frontend_flutter/services/carousel_slider_widget.dart';

//for testing widget & will remove later
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
      backgroundColor: Colors.white,
      body: Column(children: [
        //App Bar
        AppBarWidget(),
        Expanded(
            child: SingleChildScrollView(
                child: Column(
          children: [
            //Recommndation Title
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: Container(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Recommandation",
                  style: GoogleFonts.raleway(fontSize: 18),
                ),
              ),
            ),
            //Recommndation Carousel
            Padding(
              padding: const EdgeInsets.only(top: 10),
              child: CarouselSliderWidget(),
            ),
            //Incoming Title
            Padding(
              padding: const EdgeInsets.only(left: 10, top: 10),
              child: Container(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Incoming",
                  style: GoogleFonts.raleway(fontSize: 18),
                ),
              ),
            ),
            //Incoming Carousel
            Padding(
              padding: const EdgeInsets.only(top: 10),
              child: CarouselSliderWidget(),
            ),
            //Society Title
            Padding(
              padding: const EdgeInsets.only(left: 10, top: 10),
              child: Container(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Society",
                  style: GoogleFonts.raleway(fontSize: 18),
                ),
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
