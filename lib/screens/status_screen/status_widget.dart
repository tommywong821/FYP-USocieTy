import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/status_constants.dart';
import '../../model/enrolled_event/enrolled_event.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';

class StatusWidget extends StatefulWidget {
  final List<EnrolledEvent> enrolledEvent;
  const StatusWidget({super.key, required this.enrolledEvent});

  @override
  State<StatusWidget> createState() => _StatusWidgetState();
}

class _StatusWidgetState extends State<StatusWidget> {
  final double ICON_SIZE = 36.0;

  var items = ["Event Enrollment Status", "Society Enrollment Status"];
  var dropdownvalue = "Event Enrollment Status";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            backgroundColor: Styles.backGroundColor,
            centerTitle: true,
            title: StatefulBuilder(
                builder: (BuildContext context, StateSetter setState) {
              return DropdownButton(
                value: dropdownvalue,
                icon: const Icon(Icons.keyboard_arrow_down),
                items: items.map((String items) {
                  return DropdownMenuItem(
                    value: items,
                    child: Text(
                      items,
                      style: Styles.carouselTitle,
                    ),
                  );
                }).toList(),
                onChanged: (String? value) {
                  setState(() {
                    dropdownvalue = value!;
                  });
                },
              );
            })),
        body: _buildNotificationUi(widget.enrolledEvent));
  }

  ListView _buildNotificationUi(List<EnrolledEvent> enrolledEvent) {
    return ListView.builder(
      itemCount: enrolledEvent.length,
      itemBuilder: ((context, index) {
        return Column(
          children: [
            buildEventCard(index, enrolledEvent),
            Divider(
              thickness: 2,
              height: 10,
            ),
          ],
        );
      }),
    );
  }

  ListTile buildEventCard(int index, List<EnrolledEvent> enrolledEvent) {
    return ListTile(
      dense: true,
      visualDensity: VisualDensity(horizontal: 0, vertical: 0),
      contentPadding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
      leading: buildLeadingIcon(index, enrolledEvent),
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

  Icon buildLeadingIcon(int index, List<EnrolledEvent> enrolledEvent) {
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
