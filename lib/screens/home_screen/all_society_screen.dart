import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/horizontal_society_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/screen_arguments.dart';
import 'package:lazy_load_scrollview/lazy_load_scrollview.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';

class AllSocietyScreen extends StatefulWidget {
  const AllSocietyScreen({super.key});

  @override
  State<AllSocietyScreen> createState() => _AllSocietyScreenState();
}

class _AllSocietyScreenState extends State<AllSocietyScreen> {
  static int page = 0;
  static bool indicator = true;

  //get next page for lazy loading
  Future<List<Society>> getNextpage() async {
    page++;
    Future<List<Society>> eventListFuture =
        ApiService().getAllSociety(page, 10);
    return eventListFuture;
  }

  @override
  void dispose() {
    page = 0;
    indicator = true;
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final ScreenArguments screenArguments =
        ModalRoute.of(context)!.settings.arguments as ScreenArguments;
    List<Society> societyList = screenArguments.societyList;
    return Scaffold(
      body: SafeArea(
          child: Column(
        children: [
          Row(
            children: [
              IconButton(
                onPressed: (() => {Navigator.pop(context)}),
                icon: Icon(Icons.arrow_back),
                color: Colors.black,
              ),
              Text(
                "All Societies",
                style: Styles.incEventTitle,
              )
            ],
          ),
          StatefulBuilder(
              builder: (BuildContext context, StateSetter setState) {
            return Expanded(
              child: LazyLoadScrollView(
                scrollOffset: 10,
                onEndOfPage: () {
                  var snackBar = progressIndicator();
                  if (indicator)
                    ScaffoldMessenger.of(context).showSnackBar(snackBar);
                  getNextpage().then((value) {
                    //societyList = combine (societyList + value)
                    societyList = [...societyList, ...value];
                    if (value.isEmpty) indicator = false;
                  }).whenComplete(() {
                    setState(() {});
                  });
                },
                child: ListView.separated(
                  padding: const EdgeInsets.only(left: 5, right: 5),
                  itemCount: societyList.length,
                  separatorBuilder: ((context, index) {
                    return const SizedBox(height: 1);
                  }),
                  itemBuilder: ((context, index) {
                    return HorizontalSocietyCardWidget(
                      society: societyList[index],
                      eventList: screenArguments.enrolledEventList,
                    );
                  }),
                ),
              ),
            );
          }),
        ],
      )),
    );
  }
}

SnackBar progressIndicator() {
  return SnackBar(
    duration: Duration(seconds: 1),
    elevation: 0,
    backgroundColor: Colors.transparent,
    content: Column(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        CircularProgressIndicator(
          color: Styles.primaryColor,
        ),
      ],
    ),
  );
}
