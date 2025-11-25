package com.testbank.dbo.paymentservice.repository;

import com.testbank.dbo.paymentservice.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByFromAccount(String fromAccount);
    List<PaymentEntity> findByStatus(String status);
}