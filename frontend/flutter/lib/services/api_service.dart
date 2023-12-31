import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:ngok3fyp_frontend_flutter/constants.dart';
import 'package:ngok3fyp_frontend_flutter/model/auth/aad_profile.dart';
import 'package:ngok3fyp_frontend_flutter/model/auth/jwt_token.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_event/enrolled_event.dart';
import 'package:ngok3fyp_frontend_flutter/model/enrolled_society.dart';
import 'package:ngok3fyp_frontend_flutter/model/event.dart';
import 'package:ngok3fyp_frontend_flutter/model/society.dart';
import 'package:ngok3fyp_frontend_flutter/model/student.dart';

import 'storage_service.dart';

class ApiService {
  late Dio _dio;

  final backendDomain = 'ngok3fyp-backend.herokuapp.com';
  final StorageService _storageService = StorageService();

  ApiService() {
    _dio = Dio();
    initializeInterceptors();
  }

  initializeInterceptors() {
    _dio.interceptors.add(InterceptorsWrapper(
      onRequest: (options, handler) async {
        print("Request:");
        print("${options.method} ${options.path}");
        options.headers['Content-Type'] = 'application/json';
        options.headers['Cookie'] =
            await _storageService.readSecureData(COOKIE_KEY);
        options.responseType = ResponseType.plain;
        print("headers: ${options.headers}");
        print("body: ${options.data}");
        return handler.next(options);
      },
      onResponse: (response, handler) {
        print("Response:");
        print("${response.statusCode} ${response.data}");
        return handler.next(response);
      },
      onError: (DioError e, handler) {
        print("API Error:");
        print("${e.response?.statusCode} ${e.message}");
        return handler.next(e);
      },
    ));
  }

  Future<AADProfile> getUserDetails(String accessToken) async {
    final url = Uri.https('graph.microsoft.com', 'oidc/userinfo');
    final response = await _dio.getUri(url,
        options: Options(headers: {'Authorization': 'Bearer $accessToken'}));

    if (response.statusCode == 200) {
      return AADProfile.fromJson(jsonDecode(response.data));
    } else {
      throw Exception('Failed to get user details');
    }
  }

  Future<List<Event>> getAllEvent([num pageNum = -1, num pageSize = -1]) async {
    final uri = Uri.https(backendDomain, '/event', {
      'pageNum': pageNum == -1 ? '0' : pageNum.toString(),
      'pageSize': pageSize == -1 ? '10' : pageSize.toString()
    });
    final response = await _dio.getUri(uri);

    if (response.statusCode == 200) {
      List<dynamic> eventJsonList = jsonDecode(response.data);
      return eventJsonList
          .map((eventJson) => Event.fromJson(eventJson))
          .toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<Student> getStudentProfile(String itsc) async {
    final uri = Uri.https(backendDomain, '/student', {'itsc': itsc});
    final response = await _dio.getUri(uri);

    if (response.statusCode == 200) {
      return Student.fromJson(jsonDecode(response.data));
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<List<EnrolledEvent>> getAllEnrolledEvent() async {
    final uri = Uri.https(backendDomain, '/enrolledEventRecord',
        {'itsc': await _storageService.readSecureData(ITSC_KEY)});
    final response = await _dio.getUri(uri);

    if (response.statusCode == 200) {
      List<dynamic> enrolledEventJsonList = jsonDecode(response.data);
      return enrolledEventJsonList
          .map((eventJson) => EnrolledEvent.fromJson(eventJson))
          .toList();
    } else {
      throw Exception('Failed to load enrolled event by itsc:');
    }
  }

  Future<List<EnrolledSociety>> getAllEnrolledSociety() async {
    final uri = Uri.https(backendDomain, '/student/societyStatus',
        {'itsc': await _storageService.readSecureData(ITSC_KEY)});
    final response = await _dio.getUri(uri);

    if (response.statusCode == 200) {
      List<dynamic> enrolledSocietyJsonList = jsonDecode(response.data);
      return enrolledSocietyJsonList
          .map((societyJson) => EnrolledSociety.fromJson(societyJson))
          .toList();
    } else {
      throw Exception('Failed to load enrolled society by itsc:');
    }
  }

  Future<JWTToken> signCookieFromBackend(AADProfile aadProfile) async {
    final uri = Uri.https(backendDomain, '/auth/mobileLogin');
    final response = await _dio.postUri(uri, data: jsonEncode(aadProfile));

    if (response.statusCode == 200) {
      return JWTToken.fromJson(jsonDecode(response.data));
    } else {
      throw Exception('Failed to sign cookie from backend');
    }
  }

  Future<bool> registerEvent(String eventID) async {
    final url = Uri.https(backendDomain, '/event/join').toString();
    try {
      final response = await _dio.post(url, data: {
        'itsc': await _storageService.readSecureData(ITSC_KEY),
        'eventId': eventID
      });
      if (response.statusCode == 200) return true;
    } catch (e) {
      print("/event/join error");
      return false;
    }
    return false;
  }

  Future<bool> registerSociety(String societyName) async {
    final url = Uri.https(backendDomain, '/society/join').toString();
    try {
      final response = await _dio.post(url, data: {
        'itsc': await _storageService.readSecureData(ITSC_KEY),
        'societyName': societyName
      });
      if (response.statusCode == 200) return true;
    } catch (e) {
      print("/society/join error");
      return false;
    }
    return false;
  }

  Future<List<Society>> getAllSociety(
      [num pageNum = -1, num pageSize = -1]) async {
    final uri = Uri.https(backendDomain, '/society', {
      'pageNum': pageNum == -1 ? '0' : pageNum.toString(),
      'pageSize': pageSize == -1 ? '10' : pageSize.toString()
    });
    final response = await _dio.getUri(uri);
    if (response.statusCode == 200) {
      List<dynamic> societyJsonList = jsonDecode(response.data);
      return societyJsonList
          .map((socetyJson) => Society.fromJson(socetyJson))
          .toList();
    } else {
      throw Exception('Failed to load all society:');
    }
  }
}
