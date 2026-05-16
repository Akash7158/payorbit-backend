package com.payorbit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createOrder(
            Double amount
    ) throws Exception {

        RazorpayClient razorpay =
                new RazorpayClient(
                        keyId,
                        keySecret
                );

        JSONObject options =
                new JSONObject();

        options.put(
                "amount",
                amount * 100
        );

        options.put(
                "currency",
                "INR"
        );

        options.put(
                "receipt",
                "txn_123456"
        );

        Order order =
                razorpay.orders.create(
                        options
                );

        return order.toString();
    }
}