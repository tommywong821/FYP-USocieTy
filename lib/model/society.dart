class Society {
  final String id;
  final String name;

  const Society({required this.id, required this.name});

  factory Society.fromJson(Map<String, dynamic> json) {
    return Society(
      id: json['id'],
      name: json['name'],
    );
  }

  String getName(){
    return name;
  }
}