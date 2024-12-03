package com.mykare.task.controller;

import com.mykare.task.constants.AppConstants;
import com.mykare.task.dto.LoginRequestDTO;
import com.mykare.task.interfaces.AuthStrategy;
import com.mykare.task.service.AuthStrategyFactory;
import com.mykare.task.dto.UserDTO;
import com.mykare.task.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final AuthStrategyFactory authStrategyFactory;
    private final UserService userService;

    public PublicController(AuthStrategyFactory authStrategyFactory, UserService userService) {
        this.authStrategyFactory = authStrategyFactory;
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return AppConstants.WELCOME_MESSAGE;
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signUp(@Valid @RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @PostMapping("/signIn")
    public void signIn(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        AuthStrategy authStrategy = authStrategyFactory.getAuthStrategy(loginRequestDTO);
        String token = authStrategy.signIn(loginRequestDTO);
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    @PostMapping("/signOut")
    @ResponseStatus(HttpStatus.OK)
    public void signOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

