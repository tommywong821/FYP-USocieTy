class Society {
  final String id;
  final String name;
  final String description;
  final int holdingEventNumber;

  const Society(
      {required this.id,
      required this.name,
      required this.description,
      required this.holdingEventNumber});

  factory Society.fromJson(Map<String, dynamic> json) {
    return Society(
      id: json['id'],
      name: json['name'],
      description: (json['description'] != null) ? json['description'] : "",
      holdingEventNumber: json['holdingEventNumber'],
    );
  }

  String getName() {
    return name;
  }
}
