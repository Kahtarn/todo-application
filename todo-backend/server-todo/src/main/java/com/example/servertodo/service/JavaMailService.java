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
        message.setTo(toEmail);
        message.setSubject("Verify your account");
        message.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);
    }
}
