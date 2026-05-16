package com.payorbit.controller;

import com.payorbit.dto.AddMoneyRequest;
import com.payorbit.dto.BankTransferRequest;
import com.payorbit.dto.SendMoneyRequest;
import com.payorbit.entity.Transaction;
import com.payorbit.service.EmailService;
import com.payorbit.service.PdfService;
import com.payorbit.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final PdfService pdfService;

    private final EmailService emailService;


    private final TransactionService
            transactionService;

    @PostMapping("/send")
    public Transaction sendMoney(
            @RequestBody
            SendMoneyRequest request,

            Authentication authentication
    ) {

        return transactionService.sendMoney(
                request,
                authentication
        );
    }

    @GetMapping("/my")
    public List<Transaction> myTransactions(
            Authentication authentication
    ) {

        return transactionService.myTransactions(
                authentication
        );
    }
    @PostMapping("/add-money")
    public Transaction addMoney(
            @RequestBody
            AddMoneyRequest request,

            Authentication authentication
    ) {

        return transactionService.addMoney(
                request,
                authentication
        );
    }

    @GetMapping("/statement")
    public ResponseEntity<byte[]> downloadStatement(
            Authentication authentication
    ) {

        ByteArrayInputStream pdf =
                pdfService.generateStatement(
                        authentication
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=statement.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(
                        pdf.readAllBytes()
                );
    }

    @PostMapping("/email-statement")
    public String emailStatement(
            Authentication authentication
    ) {

        emailService.sendStatement(
                authentication
        );

        return "Statement Sent Successfully";
    }

    @PostMapping("/bank-transfer")
    public Transaction transferToBank(

            @RequestBody
            BankTransferRequest request,

            Authentication authentication
    ) {

        return transactionService
                .transferToBank(
                        request,
                        authentication
                );
    }

}