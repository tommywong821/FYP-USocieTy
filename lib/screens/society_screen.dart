import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/screen_arguments.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/horizontal_event_card_widget.dart';

class SocietyScreen extends StatefulWidget {
  const SocietyScreen({Key? key}) : super(key: key);

  @override
  _SocietyScreenState createState() => _SocietyScreenState();
}

class _SocietyScreenState extends State<SocietyScreen> {
  @override
  Widget build(BuildContext context) {
    final ScreenArguments screenArguments =
        ModalRoute.of(context)!.settings.arguments as ScreenArguments;
    final List<Society> societyList = screenArguments.societyList;
    final Society society = societyList[0];
    final List<Event> eventList = screenArguments.enrolledEventList;
    return Scaffold(
        bottomNavigationBar: BottomRegisterButton(context, society.name),
        body: Column(
          children: [
            Row(
              children: [
                Padding(
                  padding: const EdgeInsets.only(top: 30, left: 10),
                  child: IconButton(
                    onPressed: (() => {Navigator.pop(context)}),
                    icon: Icon(Icons.arrow_back),
                    color: Styles.primaryColor,
                  ),
                ),
              ],
            ),
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
              child: Text(society.getName(),
                  style: Styles.societyCarouselSliderTitle,
                  overflow: TextOverflow.ellipsis),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu.",
                textAlign: TextAlign.center,
                style: Styles.societyCarouselSliderDesc,
                maxLines: 3,
              ),
            ),
            Divider(
              thickness: 3,
              indent: 70,
              endIndent: 70,
              height: 20,
            ),
            Expanded(
              child: ListView.separated(
                padding: const EdgeInsets.only(left: 5, right: 5),
                itemCount: eventList.length,
                separatorBuilder: ((context, index) {
                  return const SizedBox(height: 1);
                }),
                itemBuilder: ((context, index) {
                  //TODO: mapping correct events to this society
                  return HorizontalEventCardWidget(
                    event: eventList[index],
                  );
                }),
              ),
            )
          ],
        ));
  }

  Widget BottomRegisterButton(BuildContext context, String societyName) {
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
                showDialog(
                  context: context,
                  builder: (context) => AlertDialog(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(15))),
                    title: Text(
                      'Do you want to join $societyName?',
                      style: Styles.HCardTitle,
                    ),
                    actionsAlignment: MainAxisAlignment.spaceEvenly,
                    actions: [
                      //TODO success/fail dialog
                      ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(15),
                            ),
                            backgroundColor:
                                Styles.primaryColor, // Background color
                          ),
                          onPressed: () async {
                            bool response =
                                await ApiService().registerSociety(societyName);
                            if (response) {
                              print("success");
                            } else {
                              print("fail");
                            }
                            Navigator.pop(context);
                          },
                          child: Text('    Yes    ')),
                      ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(15),
                            ),
                            backgroundColor:
                                Styles.primaryColor, // Background color
                          ),
                          onPressed: () {
                            Navigator.pop(context);
                          },
                          child: Text('    No    '))
                    ],
                  ),
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
}
