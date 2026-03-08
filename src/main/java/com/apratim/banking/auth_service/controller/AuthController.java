package com.apratim.banking.auth_service.controller;

import com.apratim.banking.auth_service.dto.LoginRequest;
import com.apratim.banking.auth_service.dto.LoginResponse;
import com.apratim.banking.auth_service.dto.RegisterRequest;
import com.apratim.banking.auth_service.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }


}
