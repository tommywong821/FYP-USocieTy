import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/status_constants.dart';
import '../../model/enrolled_event/enrolled_event.dart';
import '../../services/api_service.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';

class StatusWidget extends StatefulWidget {
  const StatusWidget({super.key});

  @override
  State<StatusWidget> createState() => _StatusWidgetState();
}

class _StatusWidgetState extends State<StatusWidget> {
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
        backgroundColor: Styles.backGroundColor,
        centerTitle: true,
        title: Text(
          "Registered Event",
          style: TextStyle(color: Styles.primaryColor),
        ),
      ),
      body: FutureBuilder<List<EnrolledEvent>>(
        future: enrolledEventFuture,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return _buildNotificationUi();
          }
          return Center(
              child: CircularProgressIndicator(
            color: Styles.primaryColor,
          ));
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
              thickness: 2,
              height: 10,
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
