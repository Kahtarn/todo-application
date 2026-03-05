class LoginResponse {
  final String refreshToken;
  final String accessToken;
  final String fullName;

  LoginResponse({
    required this.accessToken,
    required this.refreshToken,
    required this.fullName
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      accessToken: json["accessToken"] ?? "",
      refreshToken: json["refreshToken"] ?? "",
      fullName: json["fullName"] ?? ""
      );
  }
}