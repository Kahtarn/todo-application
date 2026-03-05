import 'package:application/model/login/login_request.dart';
import 'package:application/service/auth_serice.dart';
import 'package:flutter/material.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _usernameOrEmailController = TextEditingController();
  final _passwordController = TextEditingController();

  final _authSevice = AuthSerice();
  bool _isLoading = false;

  void _login() async {
    String usernameOrEmail = _usernameOrEmailController.text.trim();
    String password = _passwordController.text.trim();

    if (usernameOrEmail.isEmpty || password.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Vui lòng nhập đầy đủ thông tin!")),
      );
      return;
    }

    setState(() {
      _isLoading = true;
    });

    final request = LoginRequest(
      usernameOrEmail: usernameOrEmail,
      password: password,
    );
    final response = await _authSevice.login(request);

    setState(() {
      _isLoading = false;
    });

    if (response.success) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Chào mừng ${response.data?.fullName}")),
      );
    } else {
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text("Lỗi"),
          content: Text(response.message),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Đóng"),
            ),
          ],
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              "WELLCOME TO TODO APPLICATION",
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 40),

            TextField(
              controller: _usernameOrEmailController,
              decoration: InputDecoration(
                labelText: "Username / Email",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 24),

            TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: InputDecoration(
                labelText: "Password",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 24),

            SizedBox(
              width: double.infinity,
              height: 50,
              child: _isLoading
                  ? const Center(child: CircularProgressIndicator())
                  : ElevatedButton(
                      onPressed: _login,
                      child: const Text("LOGIN"),
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
