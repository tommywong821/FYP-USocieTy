import 'package:flutter/material.dart';

class NotificationWidget extends StatefulWidget {
  const NotificationWidget({super.key});

  @override
  State<NotificationWidget> createState() => _NotificationWidgetState();
}

class _NotificationWidgetState extends State<NotificationWidget> {
  final List data = [
    {
      "name": "test event 1",
      "poster":
          "https://hk.portal-pokemon.com/play/resources/pokedex/img/pm/285395ca77d82861fd30cea64567021a50c1169c.png",
      "location": "location 1",
      "startDate": "2023-01-06",
      "endDate": "2023-01-07",
      "category": "sport",
      "status": 'PENDING'
    },
    {
      "name": "test event 2",
      "poster":
          "https://hk.portal-pokemon.com/play/resources/pokedex/img/pm/d0ee81f16175c97770192fb691fdda8da1f4f349.png",
      "location": "location 2",
      "startDate": "2023-01-07",
      "endDate": "2023-01-08",
      "category": "hobby",
      "status": 'SUCCESS'
    },
    {
      "name": "test event 3",
      "poster":
          "https://hk.portal-pokemon.com/play/resources/pokedex/img/pm/dac21223589cec6f966b60aca69116f34d29e904.png",
      "location": "location 3",
      "startDate": "2023-01-08",
      "endDate": "2023-01-09",
      "category": "hobby",
      "status": 'DECLINE'
    },
  ];

  final double ICON_SIZE = 36.0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text("Registered Event"),
      ),
      body: ListView.builder(
        itemCount: data.length,
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
      ),
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
        children: [Text(data[index]["category"]), Text(data[index]["name"])],
      ),
      subtitle: Row(
        children: [
          Text(data[index]["startDate"]),
          SizedBox(
            width: 10,
          ),
          Text(data[index]["location"]),
        ],
      ),
    );
  }

  Icon buildLeadingIcon(int index) {
    return (data[index]['status'] == 'PENDING')
        // pending state icon
        ? Icon(
            Icons.pending_actions_rounded,
            size: ICON_SIZE,
          )
        : (data[index]['status'] == 'SUCCESS')
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
