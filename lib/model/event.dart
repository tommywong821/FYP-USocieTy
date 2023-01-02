class Event {
  final String name;
  final String poster;
  final int maxParticipation;
  final String applyDeadline;
  final String location;
  final String date;
/*missing parameters
1. event start & finish time
2. event category tag(e.g. sport, music..., maybe featured?)
3. event description
4. event fee(if any)
*/
  const Event({
    required this.name,
    required this.poster,
    required this.maxParticipation,
    required this.applyDeadline,
    required this.location,
    required this.date,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
        name: json['name'],
        poster: json['poster'],
        maxParticipation: json['maxParticipation'],
        applyDeadline: json['applyDeadline'],
        location: json['location'],
        date: json['date']);
  }

  @override
  String toString() {
    return "(name: $name, poster: $poster, maxParticipation: $maxParticipation, applyDeadline: $applyDeadline, location: $location, date: $date)";
  }
}
