import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/event_carousel_slider_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';

class TabBarWidget extends StatefulWidget {
  final List<Event> eventList;
  const TabBarWidget({Key? key, required this.eventList}) : super(key: key);

  @override
  _TabBarWidgetState createState() => _TabBarWidgetState();
}

class _TabBarWidgetState extends State<TabBarWidget>
    with TickerProviderStateMixin {
  List<Event> sportEvent = [];
  List<Event> outdoorEvent = [];
  List<Event> indoorEvent = [];

  @override
  void initState() {
    super.initState();
    sortEvent();
  }

  void sortEvent() {
    sportEvent.clear();
    outdoorEvent.clear();
    indoorEvent.clear();
    for (Event event in widget.eventList) {
      switch (event.category) {
        case "SPORT":
          sportEvent.add(event);
          break;
        case "OUTDOOR":
          outdoorEvent.add(event);
          break;
        case "INDOOR":
          indoorEvent.add(event);
          break;
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    sortEvent();
    TabController _tabController = TabController(length: 3, vsync: this);
    return StatefulBuilder(
        builder: (BuildContext context, StateSetter setState) {
      return Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: Container(
              height: 30,
              child: TabBar(
                  controller: _tabController,
                  unselectedLabelColor: Styles.primaryColor,
                  indicatorSize: TabBarIndicatorSize.label,
                  indicator: BoxDecoration(
                      borderRadius: BorderRadius.circular(50),
                      color: Styles.primaryColor),
                  tabs: [
                    Tab(
                      child: Container(
                        decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(50),
                            border: Border.all(
                                color: Styles.primaryColor, width: 1)),
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("Sport"),
                        ),
                      ),
                    ),
                    Tab(
                      child: Container(
                        decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(50),
                            border: Border.all(
                                color: Styles.primaryColor, width: 1)),
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("Outdoor"),
                        ),
                      ),
                    ),
                    Tab(
                      child: Container(
                        decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(50),
                            border: Border.all(
                                color: Styles.primaryColor, width: 1)),
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("Indoor"),
                        ),
                      ),
                    ),
                  ]),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: Container(
                height: 250,
                child: TabBarView(controller: _tabController, children: [
                  EventCarouselSliderWidget(
                    event: sportEvent,
                    reducedForm: true,
                  ),
                  EventCarouselSliderWidget(
                    event: outdoorEvent,
                    reducedForm: true,
                  ),
                  EventCarouselSliderWidget(
                    event: indoorEvent,
                    reducedForm: true,
                  )
                ])),
          )
        ],
      );
    });
  }
}
