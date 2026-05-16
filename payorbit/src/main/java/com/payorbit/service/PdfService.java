package com.payorbit.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.payorbit.entity.Transaction;
import com.payorbit.entity.User;
import com.payorbit.repository.TransactionRepository;
import com.payorbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public ByteArrayInputStream generateStatement(
            Authentication authentication
    ) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow();

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(
                                user.getEmail(),
                                user.getEmail()
                        );

        Document document =
                new Document();

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            Font heading =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20
                    );

            Paragraph title =
                    new Paragraph(
                            "PayOrbit Statement",
                            heading
                    );

            title.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(title);

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            "Name: " + user.getFullName()
                    )
            );

            document.add(
                    new Paragraph(
                            "Email: " + user.getEmail()
                    )
            );

            document.add(
                    new Paragraph(
                            "Balance: ₹" + user.getBalance()
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            PdfPTable table =
                    new PdfPTable(5);

            table.setWidthPercentage(100);

            table.addCell("Sender");
            table.addCell("Receiver");
            table.addCell("Amount");
            table.addCell("Type");
            table.addCell("Date");

            for (Transaction t : transactions) {

                table.addCell(
                        t.getSenderEmail()
                );

                table.addCell(
                        t.getReceiverEmail()
                );

                table.addCell(
                        "₹" + t.getAmount()
                );

                table.addCell(
                        t.getType()
                );

                table.addCell(
                        t.getCreatedAt().toString()
                );
            }

            document.add(table);

            document.close();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return new ByteArrayInputStream(
                out.toByteArray()
        );
    }
}