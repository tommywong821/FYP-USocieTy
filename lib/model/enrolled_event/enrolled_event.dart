class EnrolledEvent {
  final String name;
  final String poster;
  final String location;
  final String startDate;
  final String endDate;
  final String category;
  final String status;

  const EnrolledEvent({
    required this.name,
    required this.poster,
    required this.location,
    required this.startDate,
    required this.endDate,
    required this.category,
    required this.status,
  });

  factory EnrolledEvent.fromJson(Map<String, dynamic> json) {
    return EnrolledEvent(
      name: json['name'],
      poster: json['poster'],
      location: json['location'],
      startDate: json['startDate'],
      endDate: json['endDate'],
      category: json['category'],
      status: json['status'],
    );
  }

  @override
  String toString() {
    return "(name: $name, poster: $poster, maxParticipation: $location, startDate: $startDate, endDate: $endDate, category: $category, status: $status)";
  }
}
