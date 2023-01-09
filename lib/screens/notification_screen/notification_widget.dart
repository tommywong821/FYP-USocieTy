import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/status_constants.dart';

import '../../model/enrolled_event/enrolled_event.dart';
import '../../services/api_service.dart';

class NotificationWidget extends StatefulWidget {
  const NotificationWidget({super.key});

  @override
  State<NotificationWidget> createState() => _NotificationWidgetState();
}

class _NotificationWidgetState extends State<NotificationWidget> {
  final double ICON_SIZE = 36.0;
  late final Future<List<EnrolledEvent>> enrolledEventFuture;
  late final List<EnrolledEvent> enrolledEvent;

  @override
  void initState() {
    super.initState();

    initEventData();
  }

  Future<void> initEventData() async {
    enrolledEventFuture = ApiService().getAllEnrolledEvent();
    enrolledEvent = await enrolledEventFuture;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text("Registered Event"),
      ),
      body: FutureBuilder<List<EnrolledEvent>>(
        future: enrolledEventFuture,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return _buildNotificationUi();
          }
          return CircularProgressIndicator();
        },
      ),
    );
  }

  ListView _buildNotificationUi() {
    return ListView.builder(
      itemCount: enrolledEvent.length,
      itemBuilder: ((context, index) {
        return Column(
          children: [
            buildEventCard(index),
            Divider(
              color: Colors.black,
              height: 0,
            ),
          ],
        );
      }),
    );
  }

  ListTile buildEventCard(int index) {
    return ListTile(
      dense: true,
      visualDensity: VisualDensity(horizontal: 0, vertical: 0),
      contentPadding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
      leading: buildLeadingIcon(index),
      title: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(enrolledEvent[index].category),
          Text(enrolledEvent[index].name)
        ],
      ),
      subtitle: Row(
        children: [
          Text(enrolledEvent[index].startDate),
          SizedBox(
            width: 10,
          ),
          Text(enrolledEvent[index].location),
        ],
      ),
    );
  }

  Icon buildLeadingIcon(int index) {
    return (enrolledEvent[index].status == PENDING)
        // pending state icon
        ? Icon(
            Icons.pending_actions_rounded,
            size: ICON_SIZE,
          )
        : (enrolledEvent[index].status == SUCCESS)
            // success state icon
            ? Icon(
                Icons.check_circle_outline_rounded,
                size: ICON_SIZE,
                color: Colors.green,
              )
            // fail state icon
            : Icon(Icons.highlight_off_rounded,
                size: ICON_SIZE, color: Colors.red);
  }
}
