package com.mykare.task.service;

import com.mykare.task.dto.LoginRequestDTO;
import com.mykare.task.exception.PasswordMismatchException;
import com.mykare.task.interfaces.AuthStrategy;
import com.mykare.task.util.JWTTokenUtil;
import com.mykare.task.dto.UserDTO;
import com.mykare.task.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthentication implements AuthStrategy {

    private final JWTTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthentication(JWTTokenUtil jwtTokenUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String signIn(LoginRequestDTO loginRequestDTO) {
        UserDTO user = userService.findUserByEmail(loginRequestDTO.getEmail());
        if (user != null) {
            boolean doesPasswordMatch = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());
            if (doesPasswordMatch) {
                return generateToken(loginRequestDTO.getEmail());
            } else {
                throw new PasswordMismatchException("Password is not correct");
            }
        }
        throw new UserNotFoundException("User not found with email: " + loginRequestDTO.getEmail());
    }

    private String generateToken(String email) {
        return jwtTokenUtil.generateToken(email);
    }
}
