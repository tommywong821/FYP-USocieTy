import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/styles.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_screen/widget/horizontal_event_card_widget.dart';
import 'package:ngok3fyp_frontend_flutter/services/calendar_utils.dart';
import 'package:table_calendar/table_calendar.dart';

class CalendarWidget extends StatefulWidget {
  final List<Event> eventList;

  const CalendarWidget({Key? key, required this.eventList}) : super(key: key);

  @override
  _CalendarWidgetState createState() => _CalendarWidgetState();
}

class _CalendarWidgetState extends State<CalendarWidget> {
  late ValueNotifier<List<Event>> _selectedEvents;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  LinkedHashMap<DateTime, List<Event>> kEvents =
      LinkedHashMap<DateTime, List<Event>>();

  @override
  void initState() {
    super.initState();
    _selectedDay = _focusedDay;
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

  void initCalendarEvent(List<Event> eventList) {
    //count number to create flexible list dot in calendar
    var kEventSource = Map<DateTime, List<Event>>();
    eventList.forEach((element) {
      DateTime elementDate = DateFormat('M/dd/y').parse(element.startDate);
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
    return Scaffold(body: _buildFullUi());
  }

  Widget _buildFullUi() {
    initCalendarEvent(widget.eventList);
    return StatefulBuilder(
        builder: (BuildContext context, StateSetter setState) {
      return Column(
        children: [
          new TableCalendar<Event>(
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
              todayDecoration: BoxDecoration(
                  color: Colors.grey[500], shape: BoxShape.circle),
              selectedDecoration: BoxDecoration(
                  color: Styles.primaryColor, shape: BoxShape.circle),
              // Use `CalendarStyle` to customize the UI
              outsideDaysVisible: false,
            ),
            onDaySelected: _onDaySelected,
            onPageChanged: (focusedDay) {
              _focusedDay = focusedDay;
            },
          ),
          // padding
          const SizedBox(height: 8.0),
          Expanded(
            child: ValueListenableBuilder<List<Event>>(
              valueListenable: _selectedEvents,
              builder: (context, eventList, _) {
                return ListView.builder(
                  itemCount: eventList.length,
                  itemBuilder: (context, index) {
                    return HorizontalEventCardWidget(
                      event: eventList[index],
                    );
                  },
                );
              },
            ),
          )
        ],
      );
    });
  }
}
