class Society {
  final String id;
  final String name;
  final String description;

  const Society(
      {required this.id, required this.name, required this.description});

  factory Society.fromJson(Map<String, dynamic> json) {
    return Society(
      id: json['id'],
      name: json['name'],
      description: (json['description'] != null) ? json['description'] : "",
    );
  }

  String getName() {
    return name;
  }
}
