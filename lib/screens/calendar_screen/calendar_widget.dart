import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'package:ngok3fyp_frontend_flutter/services/calendar_utils.dart';
import 'package:ngok3fyp_frontend_flutter/screens/home_sceen/horizontal_card_widget.dart';

class CalendarWidget extends StatefulWidget {
  @override
  _CalendarWidgetState createState() => _CalendarWidgetState();
}

class _CalendarWidgetState extends State<CalendarWidget> {
  late final ValueNotifier<List<Event>> _selectedEvents;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  @override
  void initState() {
    super.initState();

    _selectedDay = _focusedDay;
    _selectedEvents = ValueNotifier(_getEventsForDay(_selectedDay!));
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

  void _onDaySelected(DateTime selectedDay, DateTime focusedDay) {
    if (!isSameDay(_selectedDay, selectedDay)) {
      setState(() {
        _selectedDay = selectedDay;
        _focusedDay = focusedDay;
      });
      _selectedEvents.value = _getEventsForDay(selectedDay);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          _buildTableCalendar(),
          // padding
          const SizedBox(height: 8.0),
          Expanded(
            child: _buildEventList(),
          ),
        ],
      ),
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
      builder: (context, value, _) {
        return ListView.builder(
          itemCount: value.length,
          itemBuilder: (context, index) {
            // return Container(
            //   margin: const EdgeInsets.symmetric(
            //     horizontal: 12.0,
            //     vertical: 4.0,
            //   ),
            //   decoration: BoxDecoration(
            //     border: Border.all(),
            //     borderRadius: BorderRadius.circular(12.0),
            //   ),
            //   child: ListTile(
            //     onTap: () => print('${value[index]}'),
            //     title: Text('${value[index]}'),
            //   ),
            // );
            return HorizontalCardWidget(
              eventID: index,
            );
          },
        );
      },
    );
  }
}
