import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_society.dart';
import '../../model/enrolled_event/enrolled_event.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';

class StatusWidget extends StatefulWidget {
  final List<EnrolledEvent> enrolledEvent;
  final List<EnrolledSociety> enrolledSociety;
  const StatusWidget(
      {super.key, required this.enrolledEvent, required this.enrolledSociety});

  @override
  State<StatusWidget> createState() => _StatusWidgetState();
}

class _StatusWidgetState extends State<StatusWidget> {
  final double ICON_SIZE = 36.0;

  var items = ["Event Enrollment Status", "Society Enrollment Status"];
  var dropdownvalue = "Event Enrollment Status";

  @override
  Widget build(BuildContext context) {
    return Scaffold(body:
        StatefulBuilder(builder: (BuildContext context, StateSetter setState) {
      return Column(
        children: [
          AppBar(
              backgroundColor: Styles.backGroundColor,
              centerTitle: true,
              title: DropdownButton(
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
                onChanged: (String? newValue) {
                  setState(() {
                    dropdownvalue = newValue!;
                  });
                },
              )),
          (dropdownvalue == "Event Enrollment Status")
              ? _buildEventStatusUi(widget.enrolledEvent)
              : _buildSocietyStatusUi(widget.enrolledSociety),
        ],
      );
    }));
  }

  Widget _buildEventStatusUi(
    List<EnrolledEvent> enrolledEvent,
  ) {
    return ListView.builder(
      physics: AlwaysScrollableScrollPhysics(),
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: enrolledEvent.length,
      itemBuilder: ((context, index) {
        return Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: ListTile(
                dense: true,
                visualDensity: VisualDensity(horizontal: 0, vertical: 0),
                contentPadding:
                    EdgeInsets.symmetric(horizontal: 8, vertical: 0),
                leading: (enrolledEvent[index].enrolledStatus == "PENDING")
                    // pending state icon
                    ? Icon(
                        Icons.pending_actions_rounded,
                        size: ICON_SIZE,
                      )
                    : (enrolledEvent[index].enrolledStatus == "SUCCESS")
                        // success state icon
                        ? Icon(
                            Icons.check_circle_outline_rounded,
                            size: ICON_SIZE,
                            color: Colors.green,
                          )
                        // fail state icon
                        : Icon(Icons.highlight_off_rounded,
                            size: ICON_SIZE, color: Colors.red),
                title: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [Text(enrolledEvent[index].name)],
                ),
                subtitle: Row(
                  children: [
                    Text(enrolledEvent[index].startDate),
                    SizedBox(
                      width: 10,
                    ),
                    Text(enrolledEvent[index].paymentStatus),
                  ],
                ),
              ),
            ),
            Divider(
              thickness: 2,
              height: 10,
            ),
          ],
        );
      }),
    );
  }

  Widget _buildSocietyStatusUi(
    List<EnrolledSociety> enrolledSociety,
  ) {
    return ListView.builder(
      physics: AlwaysScrollableScrollPhysics(),
      scrollDirection: Axis.vertical,
      shrinkWrap: true,
      itemCount: enrolledSociety.length,
      itemBuilder: ((context, index) {
        return Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: ListTile(
                dense: true,
                visualDensity: VisualDensity(horizontal: 0, vertical: 0),
                contentPadding:
                    EdgeInsets.symmetric(horizontal: 8, vertical: 0),
                leading: (enrolledSociety[index].enrolledStatus == "PENDING")
                    // pending state icon
                    ? Icon(
                        Icons.pending_actions_rounded,
                        size: ICON_SIZE,
                      )
                    : (enrolledSociety[index].enrolledStatus == "SUCCESS")
                        // success state icon
                        ? Icon(
                            Icons.check_circle_outline_rounded,
                            size: ICON_SIZE,
                            color: Colors.green,
                          )
                        // fail state icon
                        : Icon(Icons.highlight_off_rounded,
                            size: ICON_SIZE, color: Colors.red),
                title: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [Text(enrolledSociety[index].societyName)],
                ),
                subtitle: Row(
                  children: [
                    Text(enrolledSociety[index].registerDate),
                    SizedBox(
                      width: 10,
                    ),
                  ],
                ),
              ),
            ),
            Divider(
              thickness: 2,
              height: 10,
            ),
          ],
        );
      }),
    );
  }
}
