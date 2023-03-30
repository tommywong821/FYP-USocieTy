import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/event_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/profile_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/welcome_screen/welcome_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/home_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/incoming_event_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/society_screen.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/all_society_screen.dart';

// void main() => runApp(MyApp());
void main() async {
  await dotenv.load(fileName: '.env');
  runApp(const MyApp());
}

final navigatorKey = GlobalKey<NavigatorState>();

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Usociety',
      theme: ThemeData(
          scaffoldBackgroundColor: Color.fromARGB(255, 250, 250, 250)),
      routes: {
        '/': (context) => const WelcomeScreen(),
        '/home': (context) => const HomeScreen(),
        '/event': (context) => const EventScreen(),
        '/inc': (context) => const IncomingEvnetScreen(),
        '/society': (context) => const SocietyScreen(),
        '/allsociety': (context) => const AllSocietyScreen(),
      },
      navigatorKey: navigatorKey,
    );
  }
}
