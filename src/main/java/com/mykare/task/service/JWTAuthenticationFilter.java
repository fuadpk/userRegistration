package com.mykare.task.service;

import com.mykare.task.util.JWTTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@WebFilter
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTTokenUtil jwtTokenUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JWTAuthenticationFilter(JWTTokenUtil jwtTokenUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (isPublicEndpoint(httpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getTokenFromCookies(httpServletRequest);
        if (token != null && jwtTokenUtils.validateToken(token)) {
            String email = jwtTokenUtils.getUserNameFromToken(token);
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/public/") && (uri.equals("/public/login") || uri.equals("/public/signUp") || uri.equals("/public/signOut"));
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
