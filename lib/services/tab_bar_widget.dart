import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/services/reduced_carousel_slider_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/styles.dart';

class TabBarWidget extends StatefulWidget {
  const TabBarWidget({Key? key}) : super(key: key);

  @override
  _TabBarWidgetState createState() => _TabBarWidgetState();
}

class _TabBarWidgetState extends State<TabBarWidget>
    with TickerProviderStateMixin {
  @override
  Widget build(BuildContext context) {
    TabController _tabController = TabController(length: 3, vsync: this);
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
                          border:
                              Border.all(color: Styles.primaryColor, width: 1)),
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
                          border:
                              Border.all(color: Styles.primaryColor, width: 1)),
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
                          border:
                              Border.all(color: Styles.primaryColor, width: 1)),
                      child: Align(
                        alignment: Alignment.center,
                        child: Text("Indoor"),
                      ),
                    ),
                  ),
                ]),
          ),
        ),
        Container(
          height: 250,
          child: TabBarView(controller: _tabController, children: [
            CarouselSliderWidget(),
            CarouselSliderWidget(),
            CarouselSliderWidget()
          ]),
        )
      ],
    );
  }
}
