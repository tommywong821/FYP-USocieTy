import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/student.dart';

class ApiService {
  final backendDomain = 'ngok3fyp-backend.herokuapp.com';
  final localBackendDomain = '10.0.2.2:8080';

  Future<Map<String, dynamic>> getUserDetails(String accessToken) async {
    final url = Uri.https('graph.microsoft.com', 'oidc/userinfo');
    final response = await http.get(
      url,
      headers: {'Authorization': 'Bearer $accessToken'},
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Failed to get user details');
    }
  }

  Future<List<Event>> getAllEvent() async {
    final url =
        Uri.https(backendDomain, '/event', {'pageNum': '0', 'pageSize': '10'});
    final response = await http.get(url);

    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((eventJson) => Event.fromJson(eventJson)).toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<Student> getStudentProfile(String itsc) async {
    final url = Uri.http(localBackendDomain, '/student', {'itsc': itsc});
    final response = await http.get(url);

    if (response.statusCode == 200) {
      dynamic studentJson = jsonDecode(response.body);
      return Student.fromJson(studentJson);
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }
}
