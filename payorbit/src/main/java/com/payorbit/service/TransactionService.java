package com.payorbit.service;

import com.payorbit.dto.AddMoneyRequest;
import com.payorbit.dto.BankTransferRequest;
import com.payorbit.dto.SendMoneyRequest;
import com.payorbit.entity.Transaction;
import com.payorbit.entity.User;
import com.payorbit.repository.TransactionRepository;
import com.payorbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public Transaction sendMoney(
            SendMoneyRequest request,
            Authentication authentication
    ) {

        // sender
        User sender =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Sender not found"
                        ));

        // receiver
        User receiver =
                userRepository.findByEmail(
                        request.getReceiverEmail()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Receiver not found"
                        ));

        // balance check
        if (sender.getBalance() <
                request.getAmount()) {

            throw new RuntimeException(
                    "Insufficient Balance"
            );
        }

        // deduct sender balance
        sender.setBalance(
                sender.getBalance()
                        - request.getAmount()
        );

        // add receiver balance
        receiver.setBalance(
                receiver.getBalance()
                        + request.getAmount()
        );

        // save updated users
        userRepository.save(sender);

        userRepository.save(receiver);

        // transaction entry
        // sender transaction
        Transaction senderTransaction =
                new Transaction();

        senderTransaction.setSenderEmail(
                sender.getEmail()
        );

        senderTransaction.setReceiverEmail(
                receiver.getEmail()
        );

        senderTransaction.setAmount(
                request.getAmount()
        );

        senderTransaction.setType("DEBIT");

        senderTransaction.setStatus("SUCCESS");

        senderTransaction.setCreatedAt(
                LocalDateTime.now()
        );

        repository.save(senderTransaction);
        notificationService
                .createNotification(

                        sender.getEmail(),

                        "₹" +
                                request.getAmount() +
                                " sent to " +
                                receiver.getFullName()
                );

// receiver transaction
        Transaction receiverTransaction =
                new Transaction();

        receiverTransaction.setSenderEmail(
                sender.getEmail()
        );

        receiverTransaction.setReceiverEmail(
                receiver.getEmail()
        );

        receiverTransaction.setAmount(
                request.getAmount()
        );

        receiverTransaction.setType("CREDIT");

        receiverTransaction.setStatus("SUCCESS");

        receiverTransaction.setCreatedAt(
                LocalDateTime.now()
        );
        notificationService
                .createNotification(

                        receiver.getEmail(),

                        "₹" +
                                request.getAmount() +
                                " received from " +
                                sender.getFullName()
                );

        return repository.save(
                receiverTransaction
        );
    }

    public List<Transaction> myTransactions(
            Authentication authentication
    ) {

        return repository
                .findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(
                        authentication.getName(),
                        authentication.getName()
                );
    }

    public Transaction addMoney(
            AddMoneyRequest request,
            Authentication authentication
    ) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        // add balance
        user.setBalance(
                user.getBalance()
                        + request.getAmount()
        );

        userRepository.save(user);

        // transaction entry
        Transaction transaction =
                new Transaction();

        transaction.setSenderEmail(
                "BANK"
        );

        transaction.setReceiverEmail(
                user.getEmail()
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setType("CREDIT");

        transaction.setStatus("SUCCESS");

        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        return repository.save(transaction);
    }

    public Transaction transferToBank(

            BankTransferRequest request,

            Authentication authentication
    ) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        // balance check
        if (user.getBalance()
                < request.getAmount()) {

            throw new RuntimeException(
                    "Insufficient Balance"
            );
        }

        // deduct balance
        user.setBalance(
                user.getBalance()
                        - request.getAmount()
        );

        userRepository.save(user);

        // transaction
        Transaction transaction =
                new Transaction();

        // IMPORTANT FIX ✅
        transaction.setSenderEmail(
                user.getEmail()
        );

        transaction.setReceiverEmail(
                "BANK"
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setType(
                "BANK_TRANSFER"
        );

        transaction.setStatus(
                "PROCESSING"
        );

        transaction.setBankAccount(
                request.getAccountNumber()
        );

        transaction.setIfscCode(
                request.getIfsc()
        );

        transaction.setCreatedAt(
                java.time.LocalDateTime.now()
        );

        Transaction savedTransaction =
                repository.save(transaction);

        // fake bank processing 😎
        new Thread(() -> {

            try {

                Thread.sleep(5000);

                savedTransaction.setStatus(
                        "SUCCESS"
                );

                repository.save(
                        savedTransaction
                );

            } catch (Exception e) {

                e.printStackTrace();
            }

        }).start();

        return savedTransaction;
    }
}