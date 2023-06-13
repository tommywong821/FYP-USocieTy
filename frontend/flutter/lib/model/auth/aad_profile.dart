class AADProfile {
  final String email;
  final String itsc;
  final String name;

  const AADProfile({
    required this.email,
    required this.itsc,
    required this.name,
  });

  factory AADProfile.fromJson(Map<String, dynamic> json) {
    return AADProfile(
      email: json['email'],
      itsc: json['email'].toString().split('@')[0],
      name: json['name'],
    );
  }

  Map<String, dynamic> toJson() => {
        'name': name,
        'email': email,
        'itsc': itsc,
      };

  @override
  String toString() {
    return "(email: $email, itsc: $itsc, name: $name)";
  }
}
