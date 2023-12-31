import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/horizontal_event_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:lazy_load_scrollview/lazy_load_scrollview.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';

class IncomingEvnetScreen extends StatefulWidget {
  const IncomingEvnetScreen({Key? key}) : super(key: key);

  @override
  _IncomingEvnetScreenState createState() => _IncomingEvnetScreenState();
}

class _IncomingEvnetScreenState extends State<IncomingEvnetScreen> {
  static int page = 0;
  static bool indicator = true;

  //get next page for lazy loading
  Future<List<Event>> getNextpage() async {
    page++;
    Future<List<Event>> eventListFuture = ApiService().getAllEvent(page, 10);
    return eventListFuture;
  }

  @override
  void initState() {
    super.initState();
    page = 0;
    indicator = true;
  }

  @override
  void dispose() {
    page = 0;
    indicator = true;
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    List<Event> eventList =
        ModalRoute.of(context)!.settings.arguments as List<Event>;
    //take 10 only for lazy loading
    eventList =
        (eventList.length > 10) ? eventList.take(10).toList() : eventList;
    int eventCount = eventList.length;
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
                "Incoming Events",
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
                    //eventList = combine (eventList + value)
                    eventList = [...eventList, ...value];
                    if (value.isEmpty) indicator = false;
                  }).whenComplete(() {
                    setState(() {});
                  });
                },
                child: ListView.separated(
                  scrollDirection: Axis.vertical,
                  shrinkWrap: true,
                  padding: const EdgeInsets.only(left: 5, right: 5),
                  itemCount: eventCount,
                  separatorBuilder: ((context, index) {
                    return const SizedBox(height: 1);
                  }),
                  itemBuilder: ((context, index) {
                    return HorizontalEventCardWidget(
                      event: eventList[index],
                    );
                  }),
                ),
              ),
            );
          })
        ],
      ),
    ));
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
