import 'package:shared_preferences/shared_preferences.dart';

class SecureStorage {
  static final SecureStorage _instants = SecureStorage._internal();
  factory SecureStorage() => _instants;
  SecureStorage._internal();

  late SharedPreferences _prefs;

  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  static const String _keyAccessToken = "access_token";
  static const String _keyRefreshToken = "refresh_token";

  Future<void> saveLoginData({
    required String accessToken,
    required String refreshToken,
  }) async {
    await _prefs.setString(_keyAccessToken, accessToken);
    await _prefs.setString(_keyRefreshToken, refreshToken);
  }

  String? getAccessToken() => _prefs.getString(_keyAccessToken);
  String? getRefreshToken() => _prefs.getString(_keyRefreshToken);

  bool isLoggedIn() => getAccessToken() != null;

  Future<void> clearAll() async {
    await _prefs.clear();
  }
}