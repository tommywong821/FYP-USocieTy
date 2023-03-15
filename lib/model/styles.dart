import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class Styles {
  static Color primaryColor = const Color.fromARGB(255, 0, 51, 102);
  static Color backGroundColor = const Color.fromARGB(255, 250, 250, 250);

/* Screens */
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

//styles for screens/incoming_event_screen.dart
  static TextStyle incEventTitle = GoogleFonts.ptSans(
      fontSize: 20, fontWeight: FontWeight.w600, color: primaryColor);

/* Widgets */
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

//styles for screens/event_carousel_slider_widget.dart
  static TextStyle carouselSliderTitle = GoogleFonts.ptSans(
      fontSize: 14, fontWeight: FontWeight.w600, color: Colors.black);
  static TextStyle carouselSliderDate = GoogleFonts.ptSans(
      fontSize: 12, fontWeight: FontWeight.w500, color: primaryColor);
  static TextStyle carouselSliderLocation = GoogleFonts.ptSans(
      fontSize: 12, fontWeight: FontWeight.w600, color: Colors.grey);

//styles for screens/society_carousel_slider_widget.dart
  static TextStyle societyCarouselSliderTitle = GoogleFonts.ptSans(
      fontSize: 26, fontWeight: FontWeight.w600, color: Colors.black);
  static TextStyle societyCarouselSliderDesc = GoogleFonts.ptSans(
      fontSize: 12, fontWeight: FontWeight.w600, color: Colors.grey);

//styles for horizontal_card_widget.dart
  static TextStyle HCardTitle = GoogleFonts.ptSans(
      fontSize: 18, fontWeight: FontWeight.w600, color: Colors.black);
  static TextStyle HCardDate = GoogleFonts.ptSans(
      fontSize: 14, fontWeight: FontWeight.w500, color: primaryColor);
  static TextStyle HCardLocation = GoogleFonts.ptSans(
      fontSize: 14, fontWeight: FontWeight.w600, color: Colors.grey);
}
