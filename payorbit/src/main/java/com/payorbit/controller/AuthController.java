package com.payorbit.controller;

import com.payorbit.dto.AuthResponse;
import com.payorbit.dto.LoginRequest;
import com.payorbit.dto.LoginResponse;
import com.payorbit.dto.RegisterRequest;
import com.payorbit.entity.User;
import com.payorbit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                userService.register(request)
        );
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }
}