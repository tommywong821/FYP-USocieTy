class JWTToken {
  final String jwtToken;

  const JWTToken({
    required this.jwtToken,
  });

  factory JWTToken.fromJson(Map<String, dynamic> json) {
    return JWTToken(
      jwtToken: json['jwtToken'],
    );
  }

  @override
  String toString() {
    return "(jwtToken: $jwtToken)";
  }
}
