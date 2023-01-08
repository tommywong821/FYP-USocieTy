import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:ngok3fyp_frontend_flutter/constants.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/enrolled_event.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/student.dart';

import 'storage_service.dart';

class ApiService {
  final backendDomain = 'ngok3fyp-backend.herokuapp.com';
  final StorageService _storageService = StorageService();

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

  Future<List<Event>> getAllEvent([num pageNum = -1, num pageSize = -1]) async {
    final url = Uri.https(backendDomain, '/event', {
      'pageNum': pageNum == -1 ? '0' : pageNum.toString(),
      'pageSize': pageSize == -1 ? '10' : pageSize.toString()
    });
    final response = await http.get(url);

    if (response.statusCode == 200) {
      List<dynamic> eventJsonList = jsonDecode(response.body);
      return eventJsonList
          .map((eventJson) => Event.fromJson(eventJson))
          .toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<Student> getStudentProfile(String itsc) async {
    final url = Uri.http(backendDomain, '/student', {'itsc': itsc});
    final response = await http.get(url);

    if (response.statusCode == 200) {
      dynamic studentJson = jsonDecode(response.body);
      return Student.fromJson(studentJson);
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<List<EnrolledEvent>> getAllEnrolledEvent() async {
    final url = Uri.http(backendDomain, '/event/enrolled',
        {'itsc': await _storageService.readSecureData(ITSC_KEY)});
    final response = await http.get(url);

    if (response.statusCode == 200) {
      List<dynamic> enrolledEventJsonList = jsonDecode(response.body);
      return enrolledEventJsonList
          .map((eventJson) => EnrolledEvent.fromJson(eventJson))
          .toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }
}
