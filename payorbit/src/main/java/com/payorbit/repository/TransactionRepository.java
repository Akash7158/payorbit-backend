package com.payorbit.repository;

import com.payorbit.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction>
    findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(
            String senderEmail,
            String receiverEmail
    );
}