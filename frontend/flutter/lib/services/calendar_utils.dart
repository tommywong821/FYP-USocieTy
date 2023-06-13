import 'dart:collection';
import 'package:table_calendar/table_calendar.dart';

/// Example event class.
class EventExample {
  final String title;

  const EventExample(this.title);

  @override
  String toString() => title;
}

/// Example events.
///
/// Using a [LinkedHashMap] is highly recommended if you decide to use a map.
final kEventsExample = LinkedHashMap<DateTime, List<EventExample>>(
  equals: isSameDay,
  hashCode: getHashCode,
)..addAll(_kEventSourceExample);

final _kEventSourceExample = Map.fromIterable(
    List.generate(50, (index) => index),
    key: (item) => DateTime.utc(2023, 1, 1),
    value: (item) => List.generate(
        item % 4 + 1, (index) => EventExample('Event $item | ${index + 1}')));

int getHashCode(DateTime key) {
  return key.day * 1000000 + key.month * 10000 + key.year;
}

// calendar range
final kToday = DateTime.now();
final kFirstDay = DateTime(kToday.year, kToday.month - 3, kToday.day);
final kLastDay = DateTime(kToday.year, kToday.month + 3, kToday.day);
