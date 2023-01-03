class Event {
  final String name;
  final String poster;
  final int maxParticipation;
  final String applyDeadline;
  final String location;
  final String startDate;
  final String endDate;
  final String category;
  final String description;
  final num fee;

  const Event({
    required this.name,
    required this.poster,
    required this.maxParticipation,
    required this.applyDeadline,
    required this.location,
    required this.startDate,
    required this.endDate,
    required this.category,
    required this.description,
    required this.fee,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
      name: json['name'],
      poster: json['poster'],
      maxParticipation: json['maxParticipation'],
      applyDeadline: json['applyDeadline'],
      location: json['location'],
      startDate: json['startDate'],
      endDate: json['endDate'],
      category: json['category'],
      description: json['description'],
      fee: json['fee'],
    );
  }

  @override
  String toString() {
    return "(name: $name, poster: $poster, maxParticipation: $maxParticipation, applyDeadline: $applyDeadline, location: $location, startDate: $startDate, endDate: $endDate, category: $category, description: $description, fee: $fee)";
  }
}
