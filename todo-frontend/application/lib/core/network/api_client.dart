import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:application/core/constants/api_constants.dart';

class ApiClient {
  final Map<String, String> _defaultHeaders = {
    "Content-Type" : "application/json",
    "Accept" : "application/json"
  };

  Future<dynamic> post(String url, Map<String, dynamic> body) async {
    try {
      final response = await http.post(
        Uri.parse(url),
        headers: _defaultHeaders,
        body: jsonEncode(body)
      );
      return _processResponse(response);
    } on SocketException {
      throw Exception("Không có kết nối internet hoặc sai địa chỉ server");
    }
    catch(e) {
      rethrow;
    }
  }

  Future<dynamic> get(String url, String? token) async {
    try {
      final headers = Map<String, String>.from(_defaultHeaders);
      if(token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
      
      final response = await http.get(
        Uri.parse(url),
        headers: headers
      );
      return _processResponse(response);
    } 
    catch(e) {
      rethrow;
    }
  }

  dynamic _processResponse(http.Response response) {
    final body = jsonDecode(response.body);

    if(response.statusCode >= 200 && response.statusCode <= 300) {
      if(body["success"] == false) {
        throw Exception(body["message"] ?? "Lỗi không xác định");
      }
      return body;
    }
    else {
      throw Exception(body["message"] ?? "Lỗi server: ${response.statusCode}");
    }
  }
}