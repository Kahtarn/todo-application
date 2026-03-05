package com.example.servertodo.dto.auth.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String fullName;
    private String accessToken;
    private String refreshToken;
}
