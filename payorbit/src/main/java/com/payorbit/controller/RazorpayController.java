package com.payorbit.controller;

import com.payorbit.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class RazorpayController {

    private final RazorpayService
            razorpayService;

    @PostMapping("/create-order")
    public String createOrder(
            @RequestParam Double amount
    ) throws Exception {

        return razorpayService
                .createOrder(amount);
    }
}