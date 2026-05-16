package com.payorbit.service;

import com.payorbit.dto.LoginRequest;
import com.payorbit.dto.LoginResponse;
import com.payorbit.dto.RegisterRequest;
import com.payorbit.dto.UpdateProfileRequest;
import com.payorbit.entity.User;
import com.payorbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public User register(RegisterRequest request) {

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        String username =
                request.getEmail()
                        .split("@")[0];

        user.setUpiId(
                username + "@payorbit"
        );

        user.setRole("USER");
        user.setBalance(10000.0);

        return repository.save(user);
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        User user = repository.findByEmail(
                        request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean isMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!isMatch) {
            throw new RuntimeException("Invalid password");
        }

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return new LoginResponse(
                token,
                user.getUpiId()
        );
    }

    public User updateProfile(

            UpdateProfileRequest request,

            Authentication authentication
    ) {

        User user =
                repository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        user.setFullName(
                request.getFullName()
        );
        user.setProfilePhoto(
                request.getProfilePhoto()
        );

        // password change
        if (request.getPassword() != null
                &&
                !request.getPassword().isEmpty()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        return repository.save(user);
    }
}