import 'package:flutter/material.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';

class SocietyScreen extends StatefulWidget {
  const SocietyScreen({Key? key}) : super(key: key);

  @override
  _SocietyScreenState createState() => _SocietyScreenState();
}

class _SocietyScreenState extends State<SocietyScreen> {
  @override
  Widget build(BuildContext context) {
    final Society society =
        ModalRoute.of(context)!.settings.arguments as Society;
    return Scaffold(
        body: SafeArea(
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(10.0),
            child: SizedBox(
              width: MediaQuery.of(context).size.width - 50,
              height: 70,
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.transparent,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Icon(
                  Icons.groups_outlined,
                  size: 100,
                  color: Styles.primaryColor,
                ),
              ),
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: Text(society.getName(),
                style: Styles.societyCarouselSliderTitle,
                overflow: TextOverflow.ellipsis),
          ),
          Divider(
            thickness: 3,
            indent: 70,
            endIndent: 70,
            height: 20,
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu.",
              textAlign: TextAlign.center,
              style: Styles.societyCarouselSliderDesc,
            ),
          )
        ],
      ),
    ));
  }
}
