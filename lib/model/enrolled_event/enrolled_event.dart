class EnrolledEvent {
  final String name;
  final String location;
  final String startDate;
  final String endDate;
  final String category;
  final String status;
  final String paymentStatus;

  const EnrolledEvent(
      {required this.name,
      required this.location,
      required this.startDate,
      required this.endDate,
      required this.category,
      required this.status,
      required this.paymentStatus});

  factory EnrolledEvent.fromJson(Map<String, dynamic> json) {
    return EnrolledEvent(
      name: json['name'],
      location: json['location'],
      startDate: json['startDate'],
      endDate: json['endDate'],
      category: json['category'],
      status: json['status'],
      paymentStatus: json['paymentStatus'],
    );
  }

  @override
  String toString() {
    return "(name: $name, location: $location, startDate: $startDate, endDate: $endDate, category: $category, status: $status, paymentStatus: $paymentStatus)";
  }
}
