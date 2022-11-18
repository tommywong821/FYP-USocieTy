import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class Styles {
  static Color primaryColor = const Color.fromARGB(255, 0, 51, 102);
  static Color backGroundColor = const Color.fromARGB(255, 250, 250, 250);
  //styles for services/app_bar_widget.dart
  static TextStyle appBarDate = GoogleFonts.ptSans(
      fontSize: 14, fontWeight: FontWeight.w500, color: Colors.grey.shade500);
  static TextStyle appBarName = GoogleFonts.ptSans(
    fontSize: 24,
    fontWeight: FontWeight.w600,
    letterSpacing: -0.5,
  );
  //styles for screens/home_widget.dart
  static TextStyle carouselTitle = GoogleFonts.ptSans(
      fontSize: 20, fontWeight: FontWeight.w600, color: primaryColor);
  static TextStyle seeAll = GoogleFonts.ptSans(
      fontSize: 14, fontWeight: FontWeight.w600, color: Colors.grey);
//styles for screens/event_screen.dart
  static TextStyle eventScreenTitle = GoogleFonts.ptSans(
      fontSize: 40,
      fontWeight: FontWeight.bold,
      color: primaryColor,
      height: 1);
  static TextStyle eventScreenBlackText = GoogleFonts.ptSans(
      fontSize: 16, fontWeight: FontWeight.bold, color: Colors.black);
  static TextStyle eventScreenGreyText =
      GoogleFonts.ptSans(fontSize: 12, color: Colors.grey);
  static TextStyle eventScreenText =
      GoogleFonts.ptSans(fontSize: 14, color: Colors.black);
}
