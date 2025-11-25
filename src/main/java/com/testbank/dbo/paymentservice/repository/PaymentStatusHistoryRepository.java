package com.testbank.dbo.paymentservice.repository;

import com.testbank.dbo.paymentservice.entity.PaymentStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentStatusHistoryRepository  extends JpaRepository<PaymentStatusHistoryEntity, Long> {
    List<PaymentStatusHistoryEntity> findByPaymentIdOrderByChangedAtDesc(Long paymentId);
}
