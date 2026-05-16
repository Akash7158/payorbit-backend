package com.payorbit.dto;

import lombok.Data;

@Data
public class BankTransferRequest {

    private String accountNumber;

    private String ifsc;

    private Double amount;
}