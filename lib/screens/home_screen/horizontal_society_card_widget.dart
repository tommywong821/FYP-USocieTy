import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';

class HorizontalSocietyCardWidget extends StatefulWidget {
  final Society society;
  const HorizontalSocietyCardWidget({Key? key, required this.society})
      : super(key: key);

  @override
  State<HorizontalSocietyCardWidget> createState() =>
      _HorizontalSocietyCardWidgetState();
}

class _HorizontalSocietyCardWidgetState
    extends State<HorizontalSocietyCardWidget> {
  @override
  Widget build(BuildContext context) {
    double cardImageWidth = MediaQuery.of(context).size.width / 2.5;
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
                  child: Icon(
                    Icons.groups_outlined,
                    size: 100,
                    color: Styles.primaryColor,
                  ),
                ),
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 60, left: 20),
                    child: Container(
                      // width: MediaQuery.of(context).size.width -
                      //     (cardImageWidth + 20),
                      alignment: Alignment.centerLeft,
                      child: Row(
                        children: <Widget>[
                          Text(
                            widget.society.getName(),
                            style: Styles.HCardTitle,
                            overflow: TextOverflow.ellipsis,
                            maxLines: 1,
                          ),
                        ],
                      ),
                    ),
                  ),
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
          // Navigator.pushNamed(
          //   context,
          //   '/****',
          //   arguments: eventID
          // );
        },
      ),
    );
  }
}
