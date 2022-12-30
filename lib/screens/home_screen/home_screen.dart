import 'package:badges/badges.dart';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_sceen/home_widget.dart';
import 'package:ngok3fyp_frontend_flutter/screens/profile_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/calendar_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({
    Key? key,
  }) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 0;
  List<Widget> _pages = <Widget>[
    HomeWidget(),
    CalendarWidget(),
    Center(
      child: Text("Widget 3"),
    ),
    ProfileScreen(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: IndexedStack(
          index: _selectedIndex,
          children: _pages,
        ),
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
          icon: Badge(
            shape: BadgeShape.circle,
            position: BadgePosition.topEnd(),
            borderRadius: BorderRadius.circular(50),
            child: Icon(
              Icons.notifications,
            ),
            badgeContent: Text(
              '1',
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          label: 'Notifications',
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
