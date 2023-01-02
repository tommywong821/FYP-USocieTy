import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/app_bar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/tab_bar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/carousel_slider_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';

class HomeWidget extends StatefulWidget {
  const HomeWidget({
    Key? key,
  }) : super(key: key);
  @override
  State<HomeWidget> createState() => _HomeWidget();
}

class _HomeWidget extends State<HomeWidget> {
  var seeAllTextColor1 = true;
  var seeAllTextColor2 = true;
  late Future<List<Event>> enrolledEventListFutre;
  late List<Event> enrolledEventList;

  Future<void> initEvent() async {
    enrolledEventListFutre = ApiService().getAllEnrolledEventByItsc();
    enrolledEventList = await enrolledEventListFutre;
  }

  @override
  void initState() {
    super.initState();
    initEvent();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Styles.backGroundColor,
        body: FutureBuilder<List<Event>>(
          future: enrolledEventListFutre,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              return Column(children: [
                //App Bar
                AppBarWidget(),
                Expanded(
                    child: SingleChildScrollView(
                        child: Column(
                  children: [
                    //Feature Title
                    Padding(
                      padding: const EdgeInsets.only(left: 15, bottom: 5),
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
                    TabBarWidget(
                      event: enrolledEventList,
                    ),
                    //Incoming Title
                    Padding(
                      padding: const EdgeInsets.only(left: 15, top: 10),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Container(
                            alignment: Alignment.centerLeft,
                            child:
                                Text("Incoming", style: Styles.carouselTitle),
                          ),
                          //See All Button
                          Padding(
                            padding: const EdgeInsets.only(right: 15),
                            child: GestureDetector(
                              onTap: () {
                                Navigator.pushNamed(
                                  context,
                                  '/inc',
                                );
                              },
                              onTapUp: (details) => {
                                setState(() {
                                  seeAllTextColor1 = true;
                                  print(enrolledEventList);
                                })
                              },
                              onTapDown: (details) => {
                                setState(() {
                                  seeAllTextColor1 = false;
                                })
                              },
                              child: Container(
                                child: Text(
                                  "See All",
                                  style: GoogleFonts.ptSans(
                                      color: !seeAllTextColor1
                                          ? Styles.primaryColor
                                          : Colors.grey,
                                      fontSize: 14,
                                      fontWeight: FontWeight.w600),
                                ),
                              ),
                            ),
                          )
                        ],
                      ),
                    ),
                    //Incoming Carousel
                    //TODO: deal with incoming events
                    Padding(
                      padding: const EdgeInsets.only(top: 10),
                      child: CarouselSliderWidget(
                        event: enrolledEventList,
                      ),
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
                          //See All Button
                          Padding(
                            padding: const EdgeInsets.only(right: 15),
                            child: GestureDetector(
                              onTap: () {
                                Navigator.pushNamed(
                                  context,
                                  '/society',
                                );
                              },
                              onTapUp: (details) => {
                                setState(() {
                                  seeAllTextColor2 = true;
                                })
                              },
                              onTapDown: (details) => {
                                setState(() {
                                  seeAllTextColor2 = false;
                                })
                              },
                              child: Container(
                                child: Text(
                                  "See All",
                                  style: GoogleFonts.ptSans(
                                      color: !seeAllTextColor2
                                          ? Styles.primaryColor
                                          : Colors.grey,
                                      fontSize: 14,
                                      fontWeight: FontWeight.w600),
                                ),
                              ),
                            ),
                          )
                        ],
                      ),
                    ),
                    //Society Carousel
                    Padding(
                      padding: const EdgeInsets.only(top: 10, bottom: 10),
                      child: CarouselSliderWidget(
                        event: enrolledEventList,
                      ),
                    ),
                  ],
                ))),
              ]);
            }
            return CircularProgressIndicator();
          },
        ));
  }
}
