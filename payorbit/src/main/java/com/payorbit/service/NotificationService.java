package com.payorbit.service;

import com.payorbit.entity.Notification;
import com.payorbit.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository
            repository;

    // create notification
    public void createNotification(

            String email,

            String message
    ) {

        Notification notification =
                new Notification();

        notification.setEmail(email);

        notification.setMessage(message);

        notification.setRead(false);

        notification.setCreatedAt(
                LocalDateTime.now()
        );

        repository.save(notification);
    }

    // fetch notifications
    public List<Notification>
    myNotifications(String email) {

        return repository
                .findByEmailOrderByCreatedAtDesc(
                        email
                );
    }
}