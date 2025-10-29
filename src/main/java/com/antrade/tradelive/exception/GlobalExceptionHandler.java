package com.antrade.tradelive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        Map<String, String> error = new HashMap<>();
        String message = getTokenErrorMessage(request);
        if (message.isEmpty()) {
            message = "Access denied: Invalid or expired token";
        }
        error.put("error", message);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException e, HttpServletRequest request) {
        Map<String, String> error = new HashMap<>();
        String message = getTokenErrorMessage(request);
        if (message.isEmpty()) {
            message = "Authentication failed: Token required or invalid";
        }
        error.put("error", message);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    private String getTokenErrorMessage(HttpServletRequest request) {
        Object errorAttr = request.getAttribute("jwtError");
        return errorAttr != null ? errorAttr.toString() : "";
    }
}