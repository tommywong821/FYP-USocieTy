class EnrolledSociety {
  final String societyName;
  final String registerDate;
  final String enrolledStatus;

  const EnrolledSociety({
    required this.societyName,
    required this.registerDate,
    required this.enrolledStatus,
  });

  factory EnrolledSociety.fromJson(Map<String, dynamic> json) {
    return EnrolledSociety(
      societyName: json['societyName'],
      registerDate: json['registerDate'],
      enrolledStatus: json['enrolledStatus'],
    );
  }
}
