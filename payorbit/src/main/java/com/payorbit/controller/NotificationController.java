package com.payorbit.controller;

import com.payorbit.entity.Notification;
import com.payorbit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService
            notificationService;

    @GetMapping
    public List<Notification>
    myNotifications(
            Authentication authentication
    ) {

        return notificationService
                .myNotifications(
                        authentication.getName()
                );
    }
}