import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:ngok3fyp_frontend_flutter/Profile.dart';
import 'package:ngok3fyp_frontend_flutter/screens/welcome_screen.dart';

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
      title: 'AAD OAuth Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      routes: {
        '/': (context) => const WelcomeScreen(),
        '/home': (context) => const Profile(), // todo update to real home page
        '/profile': (context) => const Profile(),
      },
      navigatorKey: navigatorKey,
    );
  }
}
