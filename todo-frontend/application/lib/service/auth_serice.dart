import 'package:application/core/constants/api_constants.dart';
import 'package:application/core/network/api_client.dart';
import 'package:application/model/api_response.dart';
import 'package:application/model/login/login_request.dart';
import 'package:application/model/login/login_response.dart';

class AuthSerice {
  final ApiClient _apiClient = ApiClient();

  Future<ApiResponse<LoginResponse>> login(LoginRequest request) async {
    try {
      final responseMap = await _apiClient.post(ApiConstants.login, request.toJson());
      return ApiResponse<LoginResponse>.fromJson(
        responseMap,
        (json) => LoginResponse.fromJson(json)
      );
    }
    catch(e) {
      return ApiResponse(success: false,
      message: e.toString().replaceAll("Exception ", "replace"),
      data: null);
    }
  }
}