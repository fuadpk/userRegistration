package com.mykare.task.service;

import com.mykare.task.dto.LoginRequestDTO;
import com.mykare.task.interfaces.AuthStrategy;
import org.springframework.stereotype.Service;

@Service
public class AuthStrategyFactory {

    private final JwtAuthentication jwtAuthentication;
    public AuthStrategyFactory(JwtAuthentication jwtAuthentication) {
        this.jwtAuthentication = jwtAuthentication;
    }

    public AuthStrategy getAuthStrategy(LoginRequestDTO loginRequestDTO) {
        switch (loginRequestDTO.getAuthType().toString()) {
            case "JWT":
                return jwtAuthentication;
            default:
                throw new IllegalArgumentException("Invalid authentication method: " + loginRequestDTO.getAuthType());
        }
    }
}
