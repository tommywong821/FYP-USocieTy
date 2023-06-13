import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:intl/intl.dart';

class HorizontalEventCardWidget extends StatefulWidget {
  final Event event;
  const HorizontalEventCardWidget({Key? key, required this.event})
      : super(key: key);

  @override
  _HorizontalEventCardWidgetState createState() =>
      _HorizontalEventCardWidgetState();
}

class _HorizontalEventCardWidgetState extends State<HorizontalEventCardWidget> {
  @override
  Widget build(BuildContext context) {
    double cardImageWidth = MediaQuery.of(context).size.width / 2.5;
    final DateFormat dateFormatter = DateFormat('E , MMM d ');
    final DateFormat parseDateFormatter = DateFormat('dd/M/y');

    return Container(
      width: MediaQuery.of(context).size.width - 50,
      child: GestureDetector(
        //Card view
        child: Card(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              //image
              SizedBox(
                width: cardImageWidth,
                height: 150,
                child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: Colors.transparent,
                        width: 10,
                      ),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: ClipRRect(
                      child: Image.network(
                        widget.event.poster,
                        fit: BoxFit.fitWidth,
                      ),
                      borderRadius: BorderRadius.circular(20),
                    )),
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 25, left: 10),
                    child: Container(
                      width: MediaQuery.of(context).size.width -
                          (cardImageWidth + 30),
                      alignment: Alignment.centerLeft,
                      child: Row(
                        children: <Widget>[
                          Flexible(
                            child: Text(
                              widget.event.name,
                              style: Styles.HCardTitle,
                              overflow: TextOverflow.ellipsis,
                              maxLines: 1,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 15, left: 10),
                    child: Container(
                      child: Text(dateFormatter
                          .format(DateTime.parse(parseDateFormatter
                              .parse(widget.event.startDate)
                              .toString()))
                          .toString()),
                      alignment: Alignment.centerLeft,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 15),
                    child: Row(
                      children: [
                        Icon(
                          Icons.location_pin,
                          color: Styles.primaryColor,
                        ),
                        Padding(
                          padding: const EdgeInsets.only(left: 5),
                          child: Container(
                            child: Text(
                              widget.event.location,
                              style: Styles.HCardLocation,
                            ),
                            alignment: Alignment.centerLeft,
                          ),
                        ),
                      ],
                    ),
                  )
                ],
              )
            ],
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20.0),
          ),
          elevation: 0,
        ),
        onTap: () {
          Navigator.pushNamed(context, '/event', arguments: widget.event);
        },
      ),
    );
  }
}
