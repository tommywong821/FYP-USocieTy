import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/data.dart';
import 'package:google_fonts/google_fonts.dart';

class EventScreen extends StatefulWidget {
  const EventScreen({
    Key? key,
  }) : super(key: key);

  @override
  _EventScreenState createState() => _EventScreenState();
}

class _EventScreenState extends State<EventScreen> {
  @override
  Widget build(BuildContext context) {
    Color hkustColor = Color.fromARGB(255, 0, 51, 102);
    final int eventID =
        int.parse(ModalRoute.of(context)!.settings.arguments.toString()) - 1;
    String eventImage = data[eventID]["image"];
    String eventTitle = data[eventID]["title"];
    String eventContent = data[eventID]["content"];
    return Scaffold(
        bottomNavigationBar: BottomRegisterButton(),
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
                              child: Text(
                                eventTitle,
                                style: GoogleFonts.ptSans(
                                    fontSize: 40,
                                    fontWeight: FontWeight.bold,
                                    color: hkustColor,
                                    height: 1),
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
                                    backgroundColor: hkustColor,
                                  ),
                                  Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      //Society name
                                      Padding(
                                        padding: const EdgeInsets.only(
                                          left: 15,
                                        ),
                                        child: Text("Lorem ipsum Soceity",
                                            style: GoogleFonts.ptSans(
                                              fontSize: 16,
                                              fontWeight: FontWeight.bold,
                                            )),
                                      ),
                                      //Society events count
                                      Padding(
                                        padding:
                                            const EdgeInsets.only(left: 15),
                                        child: Text(
                                          "10 Upcoming Events",
                                          style: GoogleFonts.ptSans(
                                              fontSize: 12, color: Colors.grey),
                                        ),
                                      ),
                                    ],
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
                          //Date & time
                          Padding(
                            padding: const EdgeInsets.only(left: 20),
                            child: Row(
                              children: [
                                Icon(Icons.calendar_month),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    //Event date
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        left: 15,
                                      ),
                                      child: Text("Mon, Nov 28",
                                          style: GoogleFonts.ptSans(
                                            fontSize: 16,
                                            fontWeight: FontWeight.bold,
                                          )),
                                    ),
                                    //Event time
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "14:00 - 20:00 GMT+8",
                                        style: GoogleFonts.ptSans(
                                            fontSize: 12, color: Colors.grey),
                                      ),
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
                                      child: Text("HKUST Art Hall",
                                          style: GoogleFonts.ptSans(
                                            fontSize: 16,
                                            fontWeight: FontWeight.bold,
                                          )),
                                    ),
                                    //Adrress
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "Lift 11",
                                        style: GoogleFonts.ptSans(
                                            fontSize: 12, color: Colors.grey),
                                      ),
                                    ),
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
                                      child: Text("\$100 per Member",
                                          style: GoogleFonts.ptSans(
                                            fontSize: 16,
                                            fontWeight: FontWeight.bold,
                                          )),
                                    ),
                                    //Price details
                                    Padding(
                                      padding: const EdgeInsets.only(left: 15),
                                      child: Text(
                                        "\$150 per Non-member",
                                        style: GoogleFonts.ptSans(
                                            fontSize: 12, color: Colors.grey),
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(top: 20, left: 20),
                            child: Text("Description",
                                style: GoogleFonts.ptSans(
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                )),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(
                                top: 20, left: 20, bottom: 20),
                            child: Text(eventContent,
                                style: GoogleFonts.ptSans(
                                  fontSize: 14,
                                )),
                          ),
                        ]),
                  )),
            ),
          ]),
        ));
  }
}

Widget BottomRegisterButton() {
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
            onPressed: () {/* TODO */},
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
