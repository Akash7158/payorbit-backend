package com.payorbit.controller;

import com.payorbit.dto.UpdateProfileRequest;
import com.payorbit.entity.User;
import com.payorbit.repository.UserRepository;
import com.payorbit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserService userService;


    @GetMapping("/profile")
    public User profile(
            Authentication authentication) {

        return repository.findByEmail(
                authentication.getName()
        ).orElseThrow();
    }

    @PutMapping("/update")
    public User updateProfile(

            @RequestBody
            UpdateProfileRequest request,

            Authentication authentication
    ) {

        return userService.updateProfile(
                request,
                authentication
        );
    }
}