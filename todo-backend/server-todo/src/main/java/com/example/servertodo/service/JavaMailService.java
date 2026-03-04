package com.example.servertodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpRegister(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("[ServerTodo] Email Verification Code");

        message.setText(
                "Hello,\n\n" +
                        "Thank you for registering at ServerTodo.\n\n" +
                        "Your verification code is: " + otp + "\n" +
                        "This code will expire in 5 minutes.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "ServerTodo Team"
        );

        javaMailSender.send(message);
    }
}
