package com.antrade.tradelive.web;

import com.antrade.tradelive.dto.HelloDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping(path = "/hello")
    @PreAuthorize("isAuthenticated()")  // Requires valid JWT token
    public HelloDTO hello() {
        return new HelloDTO("Hello World!");
    }
}