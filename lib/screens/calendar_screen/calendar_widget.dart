import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:ngok3fyp_frontend_flutter/model/event/event.dart';
import 'package:ngok3fyp_frontend_flutter/screens/calendar_screen/horizontal_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/api_service.dart';
import 'package:ngok3fyp_frontend_flutter/services/calendar_utils.dart';
import 'package:table_calendar/table_calendar.dart';

class CalendarWidget extends StatefulWidget {
  @override
  _CalendarWidgetState createState() => _CalendarWidgetState();
}

class _CalendarWidgetState extends State<CalendarWidget> {
  late final ValueNotifier<List<Event>> _selectedEvents;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  late Future<List<Event>> eventListFutre;
  late List<Event> eventList;
  LinkedHashMap<DateTime, List<Event>> kEvents =
      LinkedHashMap<DateTime, List<Event>>();

  @override
  void initState() {
    super.initState();

    _selectedDay = _focusedDay;
    initCalendarEvent();
  }

  @override
  void dispose() {
    _selectedEvents.dispose();
    super.dispose();
  }

  List<Event> _getEventsForDay(DateTime day) {
    // Implementation example
    return kEvents[day] ?? [];
  }

  Future<void> initCalendarEvent() async {
    eventListFutre = ApiService().getAllEvent();
    eventList = await eventListFutre;
    //count number to create flexible list dot in calendar
    final kEventSource = Map<DateTime, List<Event>>();
    eventList.forEach((element) {
      DateTime elementDate =
          DateFormat('yyyy-mm-dd').parse(element.startDate).toUtc();
      if (!kEventSource.containsKey(elementDate)) {
        kEventSource[elementDate] = List<Event>.generate(1, (index) => element);
      } else {
        kEventSource[elementDate]!.add(element);
      }
    });
    kEvents = LinkedHashMap(
      equals: isSameDay,
      hashCode: getHashCode,
    )..addAll(kEventSource);

    // print("kEvents: $kEvents");
    //update choosen card view
    _selectedEvents = ValueNotifier(_getEventsForDay(_selectedDay!));
  }

  void _onDaySelected(DateTime selectedDay, DateTime focusedDay) {
    if (!isSameDay(_selectedDay, selectedDay)) {
      setState(() {
        _selectedDay = selectedDay;
        _focusedDay = focusedDay;
      });
      _selectedEvents.value = _getEventsForDay(selectedDay);
      print('_selectedEvents.value: ${_selectedEvents.value}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder<List<Event>>(
        future: eventListFutre,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return _buildFullUi();
          }
          return CircularProgressIndicator();
        },
      ),
    );
  }

  Widget _buildFullUi() {
    return Column(
      children: [
        _buildTableCalendar(),
        // padding
        const SizedBox(height: 8.0),
        Expanded(
          child: _buildEventList(),
        )
      ],
    );
  }

  Widget _buildTableCalendar() {
    return TableCalendar<Event>(
      headerStyle: HeaderStyle(
        formatButtonVisible: false,
      ),
      firstDay: kFirstDay,
      lastDay: kLastDay,
      focusedDay: _focusedDay,
      selectedDayPredicate: (day) => isSameDay(_selectedDay, day),
      calendarFormat: CalendarFormat.month,
      // controll event dot in caldendar
      eventLoader: _getEventsForDay,
      startingDayOfWeek: StartingDayOfWeek.monday,
      calendarStyle: CalendarStyle(
        // Use `CalendarStyle` to customize the UI
        outsideDaysVisible: false,
      ),
      onDaySelected: _onDaySelected,
      onPageChanged: (focusedDay) {
        _focusedDay = focusedDay;
      },
    );
  }

  Widget _buildEventList() {
    return ValueListenableBuilder<List<Event>>(
      valueListenable: _selectedEvents,
      builder: (context, eventList, _) {
        return ListView.builder(
          itemCount: eventList.length,
          itemBuilder: (context, index) {
            return HorizontalCardWidget(
              eventID: index,
              eventObj: eventList[index],
            );
          },
        );
      },
    );
  }
}
