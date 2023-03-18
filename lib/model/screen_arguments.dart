import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';

class ScreenArguments {
  List<Event> enrolledEventList;
  List<Society> societyList;

  ScreenArguments(this.enrolledEventList, this.societyList);
}
