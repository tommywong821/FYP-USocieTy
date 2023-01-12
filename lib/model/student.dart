class Student {
  final String itsc;
  final String nickname;
  final String mail;
  final String enrolledSocieties;

  const Student(
      {required this.itsc,
      required this.nickname,
      required this.mail,
      required this.enrolledSocieties});

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      itsc: json['itsc'],
      nickname: json['nickname'],
      mail: json['mail'],
      enrolledSocieties: json['enrolledSocieties'],
    );
  }

  @override
  String toString() {
    return "(itsc: $itsc, nickname: $nickname, mail: $mail, enrolledSociety: $enrolledSocieties)";
  }
}
