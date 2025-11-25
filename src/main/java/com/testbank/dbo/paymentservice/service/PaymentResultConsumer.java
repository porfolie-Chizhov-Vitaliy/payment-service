package com.testbank.dbo.paymentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbank.dbo.paymentservice.dto.PaymentResult;
import com.testbank.dbo.paymentservice.entity.PaymentEntity;
import com.testbank.dbo.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentResultConsumer {
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PaymentHistoryStatusService paymentHistoryStatusService;
    public PaymentResultConsumer(PaymentRepository paymentRepository, ObjectMapper objectMapper,PaymentHistoryStatusService paymentHistoryStatusService) {
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
        this.paymentHistoryStatusService=paymentHistoryStatusService;
    }

    @KafkaListener(topics = "payment-results", groupId = "payment-service")
    @Transactional
    public void handlePaymentResult(String message) {
        try {
            // Парсим JSON строку в объект
            PaymentResult result = objectMapper.readValue(message, PaymentResult.class);
            System.out.println("🎯 Получен результат платежа: " + result.getStatus());
            if ("SUCCESS".equals(result.getStatus())) {
                updatePaymentStatus(result.getPaymentId(), "COMPLETED", result.getMessage());
                paymentHistoryStatusService.updatePaymentHistoryStatus(result.getPaymentId(), "COMPLETED", result.getMessage());

            } else if ("FAILED".equals(result.getStatus())) {
                updatePaymentStatus(result.getPaymentId(), "FAILED", result.getMessage());
                paymentHistoryStatusService.updatePaymentHistoryStatus(result.getPaymentId(), "FAILED", result.getMessage());
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка обработки результата платежа: " + e.getMessage());
        }
    }

    private void updatePaymentStatus(Long paymentId, String status, String description) {
        Optional<PaymentEntity> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            PaymentEntity payment = paymentOpt.get();
            payment.setStatus(status);
            payment.setDescription(description);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            System.out.println("✅ Статус платежа " + paymentId + " обновлен на " + status + ": " + description);
        } else {
            System.out.println("❌ Платеж не найден: " + paymentId);
        }
    }
}