package com.example.servertodo.service;

import com.example.servertodo.dto.auth.login.LoginRequest;
import com.example.servertodo.dto.auth.login.LoginResponse;
import com.example.servertodo.dto.auth.register.EmailVerifyRequest;
import com.example.servertodo.dto.auth.register.RegisterRequest;
import com.example.servertodo.entity.AppUser;
import com.example.servertodo.entity.EmailOtp;
import com.example.servertodo.entity.RefreshToken;
import com.example.servertodo.repository.AppUserRepository;
import com.example.servertodo.repository.EmailOtpRepository;
import com.example.servertodo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AppUserRepository appUserRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailOtpRepository emailOtpRepo;
    @Autowired
    private JavaMailService javaMailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    //Register
    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private void sendOtpToRegister(AppUser user) {
        Optional<EmailOtp> lastOtp = emailOtpRepo.findTopByUserOrderByIdDesc(user);

        if (lastOtp.isPresent()) {
            long createdTime = lastOtp.get().getExpiryTime().getTime() - (5 * 60 * 1000);
            long now = System.currentTimeMillis();

            if (now - createdTime < 30_000) {
                throw new RuntimeException("Please wait 30 seconds before requesting new OTP");
            }
            emailOtpRepo.deleteByUser(user);
        }
        String otp = generateOtp();
        long expiry = System.currentTimeMillis() + (5 * 60 * 1000);

        EmailOtp emailOtp = EmailOtp.builder()
                .otpCode(otp)
                .expiryTime(new Timestamp(expiry))
                .user(user)
                .build();

        emailOtpRepo.save(emailOtp);
        javaMailService.sendOtpRegister(user.getEmail(), otp);
    }

    @Transactional
    public void register(RegisterRequest request) {
        Optional<AppUser> userOpt = appUserRepo.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            AppUser existingUser = userOpt.get();

            if (Boolean.TRUE.equals(existingUser.getIsVerify())) {
                throw new RuntimeException("Email already verified");
            }
            sendOtpToRegister(existingUser);
            return;
        }

        if(appUserRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException(("Username already exist"));
        }

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerify(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        appUserRepo.save(user);
        sendOtpToRegister(user);
    }

    @Transactional
    public void verify(EmailVerifyRequest request) {

        AppUser user = appUserRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsVerify())) {
            throw new RuntimeException("Account already verified");
        }

        EmailOtp emailOtp = emailOtpRepo
                .findTopByUserOrderByIdDesc(user)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (emailOtp.getExpiryTime()
                .before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("OTP expired");
        }

        if (!emailOtp.getOtpCode().equals(request.getOtpCode())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setIsVerify(true);
        emailOtpRepo.deleteByUser(user);
    }

    //Login
    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepo.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("Username or email not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or password incorrect");
        }

        if(!Boolean.TRUE.equals(user.getIsVerify())) {
            throw new RuntimeException("Your account is not verify");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        Timestamp expiry = new Timestamp(
                System.currentTimeMillis() + jwtService.getRefreshExpiryTime()
        );

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiry(expiry)
                .isRevoked(false)
                .build();

        refreshTokenRepo.save(token);
        return new LoginResponse(user.getFullName() ,accessToken, refreshToken);
    }

}
