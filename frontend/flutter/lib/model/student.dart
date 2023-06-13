class Student {
  final String itsc;
  final String nickname;
  final String mail;
  final List<String> enrolledSocieties;
  final List<String> roles;

  const Student(
      {required this.itsc,
      required this.nickname,
      required this.mail,
      required this.enrolledSocieties,
      required this.roles});

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      itsc: json['itsc'],
      nickname: json['nickname'],
      mail: json['mail'],
      enrolledSocieties: List<String>.from(json['enrolledSocieties']),
      roles: List<String>.from(json['roles']),
    );
  }

  @override
  String toString() {
    return "(itsc: $itsc, nickname: $nickname, mail: $mail, enrolledSociety: $enrolledSocieties), roles: $roles";
  }
}
