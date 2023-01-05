class Student {
  final String itsc;
  final String nickname;
  final String mail;
  final String role;
  // TODO need joined society

  const Student({
    required this.itsc,
    required this.nickname,
    required this.mail,
    required this.role,
  });

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      itsc: json['itsc'],
      nickname: json['nickname'],
      mail: json['mail'],
      role: json['role'],
    );
  }

  @override
  String toString() {
    return "(itsc: $itsc, nickname: $nickname, mail: $mail, role: $role)";
  }
}
