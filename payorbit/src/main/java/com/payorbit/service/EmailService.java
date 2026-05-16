package com.payorbit.service;

import com.payorbit.entity.User;
import com.payorbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final PdfService pdfService;

    private final UserRepository userRepository;

    public void sendStatement(
            Authentication authentication
    ) {

        try {

            User user =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElseThrow();

            ByteArrayInputStream pdf =
                    pdfService.generateStatement(
                            authentication
                    );

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setTo(
                    user.getEmail()
            );

            helper.setSubject(
                    "PayOrbit Statement"
            );

            helper.setText(
                    "Attached is your PayOrbit Statement PDF."
            );

            helper.addAttachment(
                    "statement.pdf",
                    new ByteArrayResource(
                            pdf.readAllBytes()
                    )
            );

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}