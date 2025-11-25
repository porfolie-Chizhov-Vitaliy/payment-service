package com.testbank.dbo.paymentservice.service;

import com.testbank.dbo.paymentservice.entity.PaymentEntity;
import com.testbank.dbo.paymentservice.entity.PaymentStatusHistoryEntity;
import com.testbank.dbo.paymentservice.repository.PaymentRepository;
import com.testbank.dbo.paymentservice.repository.PaymentStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentHistoryStatusService {
    private final PaymentRepository paymentRepository;
    private final PaymentStatusHistoryRepository historyRepository;

    public PaymentHistoryStatusService(PaymentRepository paymentRepository, PaymentStatusHistoryRepository historyRepository) {
        this.paymentRepository = paymentRepository;
        this.historyRepository = historyRepository;
    }





    @Transactional

    public void updatePaymentHistoryStatus(Long paymentId, String newStatus, String reason) {
        Optional<PaymentEntity> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            PaymentEntity payment = paymentOpt.get();
            String oldStatus = payment.getStatus();

            // Обновляем платеж
            payment.setStatus(newStatus);
            payment.setDescription(reason);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Сохраняем историю
            saveStatusHistory(paymentId, newStatus, reason);
        }
    }

    private void saveStatusHistory(Long paymentId,
                                   String newStatus, String reason) {
        PaymentStatusHistoryEntity history = new PaymentStatusHistoryEntity();
        history.setPaymentId(paymentId);
        history.setStatus(newStatus);
        history.setReason(reason);
        history.setChangedAt(LocalDateTime.now());
        historyRepository.save(history);
    }
}
