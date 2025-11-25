package com.testbank.dbo.paymentservice.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Payment Service is UP and running!";
    }

    @GetMapping("/api/payments/hello")
    public String hello() {
        return "Hello from Payment Service!";
    }
}