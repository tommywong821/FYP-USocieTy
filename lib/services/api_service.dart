import 'dart:convert';
import 'dart:ffi';

import 'package:http/http.dart' as http;
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/services/calendar_utils.dart';

class ApiService {
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

  Future<List<Event>> getAllEnrolledEventByItsc() async {
    final response = await http.get(Uri.parse(
        'https://ngok3fyp-backend.herokuapp.com/event?pageNum=0&pageSize=10'));

    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((eventJson) => Event.fromJson(eventJson)).toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }
}
