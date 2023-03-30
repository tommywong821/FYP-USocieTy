import 'package:ngok3fyp_frontend_flutter/model/profile_screen_arguments.dart';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/enrolled_event.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_society.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/home_widget.dart';
import 'package:ngok3fyp_frontend_flutter/screens/profile_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/calendar_screen/calendar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';

import '../status_screen/status_widget.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({
    Key? key,
  }) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late Future<List<Event>> eventListFuture;
  late List<Event> eventList;
  late Future<List<Society>> societyFuture;
  late List<Society> societyList;
  late Future<List<EnrolledEvent>> enrolledEventFuture;
  late List<EnrolledEvent> enrolledEventList;
  late Future<List<EnrolledSociety>> enrolledSocietyFuture;
  late List<EnrolledSociety> enrolledSocietyList;
  List<String> enrolledSuccessSociety = [];

  int _selectedIndex = 0;

  Future<void> initEvent() async {
    eventListFuture = ApiService().getAllEvent(0, 100);
    eventList = await eventListFuture;
  }

  Future<void> initSociety() async {
    societyFuture = ApiService().getAllSociety();
    societyList = await societyFuture;
  }

  Future<void> initEnrolledEvent() async {
    enrolledEventFuture = ApiService().getAllEnrolledEvent();
    enrolledEventList = await enrolledEventFuture;
  }

  Future<void> initEnrolledSociety() async {
    enrolledSocietyFuture = ApiService().getAllEnrolledSociety();
    enrolledSocietyList = await enrolledSocietyFuture;
  }

  List<String> getEnrolledSuccessSociety(
      List<EnrolledSociety> enrolledSocietyList) {
    List<String> enrolledSuccessSociety = [];
    for (EnrolledSociety society in enrolledSocietyList) {
      if (society.enrolledStatus == "SUCCESS")
        enrolledSuccessSociety.add(society.societyName);
    }
    return enrolledSuccessSociety;
  }

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  void initState() {
    initEvent();
    initSociety();
    initEnrolledEvent();
    initEnrolledSociety();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: FutureBuilder<List<dynamic>>(
            future: Future.wait([
              eventListFuture,
              societyFuture,
              enrolledEventFuture,
              enrolledSocietyFuture
            ]),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                enrolledSuccessSociety =
                    getEnrolledSuccessSociety(enrolledSocietyList);
                List<Widget> _pages = <Widget>[
                  HomeWidget(eventList: eventList, societyList: societyList),
                  CalendarWidget(
                    eventList: eventList,
                  ),
                  StatusWidget(
                    enrolledEvent: enrolledEventList,
                    enrolledSociety: enrolledSocietyList,
                  ),
                  ProfileScreen(
                    enrolledSociety: enrolledSuccessSociety,
                  ),
                ];
                return RefreshIndicator(
                  color: Styles.primaryColor,
                  onRefresh: () async {
                    initEvent();
                    initSociety();
                    initEnrolledEvent();
                    initEnrolledSociety();
                    enrolledSuccessSociety =
                        getEnrolledSuccessSociety(enrolledSocietyList);
                    setState(() {});
                  },
                  child: IndexedStack(
                    index: _selectedIndex,
                    children: _pages,
                  ),
                );
              } else {
                return Center(
                    child: CircularProgressIndicator(
                  color: Styles.primaryColor,
                ));
              }
            }),
        bottomNavigationBar: BottomNavigationBar(
          type: BottomNavigationBarType.fixed,
          items: _bottomNavigationBarItem,
          selectedItemColor: Styles.primaryColor,
          unselectedItemColor: Colors.grey.withOpacity(0.5),
          backgroundColor: Styles.backGroundColor,
          showSelectedLabels: false,
          showUnselectedLabels: false,
          currentIndex: _selectedIndex,
          elevation: 0,
          onTap: _onItemTapped,
        ),
      ),
    );
  }

  List<BottomNavigationBarItem> get _bottomNavigationBarItem {
    return <BottomNavigationBarItem>[
      BottomNavigationBarItem(
          icon: Icon(
            Icons.home,
          ),
          label: 'Home',
          backgroundColor: Colors.white),
      BottomNavigationBarItem(
          icon: Icon(
            Icons.calendar_month,
          ),
          label: 'Calendar',
          backgroundColor: Colors.white),
      BottomNavigationBarItem(
          icon: Icon(Icons.fact_check),
          label: 'Status',
          backgroundColor: Colors.white),
      BottomNavigationBarItem(
          icon: Icon(
            Icons.person,
          ),
          label: 'Profile',
          backgroundColor: Colors.white),
    ];
  }
}
