import 'package:badges/badges.dart';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/home_widget.dart';
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
  late Future<List<Event>> enrolledEventListFuture;
  late List<Event> enrolledEventList;
  late Future<List<Society>> societyFuture;
  late List<Society> societyList;
  int _selectedIndex = 0;

  Future<void> initEvent() async {
    enrolledEventListFuture = ApiService().getAllEvent();
    enrolledEventList = await enrolledEventListFuture;
  }

  Future<void> initSociety() async {
    societyFuture = ApiService().getAllSociety();
    societyList = await societyFuture;
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
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: FutureBuilder<List<dynamic>>(
            future: Future.wait([enrolledEventListFuture, societyFuture]),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                List<Widget> _pages = <Widget>[
                  HomeWidget(
                      enrolledEventList: enrolledEventList,
                      societyList: societyList),
                  CalendarWidget(enrolledEventList: enrolledEventList),
                  StatusWidget(),
                  ProfileScreen(),
                ];
                return RefreshIndicator(
                  color: Styles.primaryColor,
                  onRefresh: () async {
                    initEvent();
                    initSociety();
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
