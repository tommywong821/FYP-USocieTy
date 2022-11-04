import 'package:badges/badges.dart';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_widget.dart';
import 'package:ngok3fyp_frontend_flutter/screens/profile_screen.dart';
import 'package:table_calendar/table_calendar.dart';

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
    TableCalendar(
      firstDay: DateTime.utc(2010, 10, 16),
      lastDay: DateTime.utc(2030, 3, 14),
      focusedDay: DateTime.now(),
    ),
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
          //animationed navigation bar
          type: BottomNavigationBarType.shifting,
          items: _bottomNavigationBarItem,
          selectedItemColor: Colors.blue,
          unselectedItemColor: Colors.grey,
          currentIndex: _selectedIndex,
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
