package com.payorbit.controller;

import com.payorbit.entity.Transaction;
import com.payorbit.entity.User;
import com.payorbit.repository.TransactionRepository;
import com.payorbit.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    public AdminController(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {

        return "Welcome Admin";
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        List<User> users =
                userRepository.findAll();

        List<Transaction> transactions =
                transactionRepository.findAll();

        double totalMoney = 0;

        for (Transaction tx : transactions) {

            totalMoney += tx.getAmount();
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "totalUsers",
                users.size()
        );

        response.put(
                "totalTransactions",
                transactions.size()
        );

        response.put(
                "totalMoneyTransferred",
                totalMoney
        );

        response.put(
                "users",
                users
        );

        response.put(
                "transactions",
                transactions
        );

        return response;
    }
}