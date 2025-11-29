package com.testbank.dbo.paymentservice.controller;

import com.testbank.dbo.paymentservice.dto.PaymentEvent;
import com.testbank.dbo.paymentservice.entity.PaymentEntity;
import com.testbank.dbo.paymentservice.repository.PaymentRepository;
import com.testbank.dbo.paymentservice.repository.PaymentStatusHistoryRepository;
import com.testbank.dbo.paymentservice.service.PaymentEventProducer;
import com.testbank.dbo.paymentservice.service.PaymentHistoryStatusService;
import com.testbank.dbo.paymentservice.validation.PaymentValidationResult;
import com.testbank.dbo.paymentservice.validation.PaymentValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Payment API", description = "Операции с платежами")
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentStatusHistoryRepository paymentStatusHistoryRepository;
    @Autowired
    private PaymentValidator paymentValidator;

    @Autowired
    private PaymentEventProducer paymentEventProducer; // ← ДОБАВЛЯЕМ
    @Operation(summary = "Get all payments", description = "Показать все платежи")
    @ApiResponse(responseCode = "200", description = "Отображение всех платежей")
    @GetMapping
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }
    @Operation(summary = "Create new payment", description = "Создание нового платежа")
    @ApiResponse(responseCode = "200", description = "Платеж создался")
    @ApiResponse(responseCode = "400", description = "Неверные платежные данные")
    @PostMapping
    public Object createPayment(@RequestBody Map<String, Object> request) {
        // 1. Получаем данные из запроса
        String fromAccount = (String) request.get("fromAccount");
        String toAccount = (String) request.get("toAccount");
        Object amountObj = request.get("amount");
        BigDecimal amount = new BigDecimal(amountObj.toString());
        //BigDecimal amount = (BigDecimal) request.get("amount");

        // 2. Проверяем валидатором
        PaymentValidationResult validation = paymentValidator.validatePayment(fromAccount, toAccount, amount);

        PaymentHistoryStatusService paymentHistoryStatusService = new PaymentHistoryStatusService(paymentRepository, paymentStatusHistoryRepository);


        // 3. Если ошибка НЕ для БД - просто возвращаем ошибку
        if (!validation.isValid() && !validation.shouldSaveToDatabase()) {
            return Map.of("error", validation.getErrorMessage());
        }

        // 4. Создаем объект платежа
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setFromAccount(fromAccount);
        paymentEntity.setToAccount(toAccount);
        paymentEntity.setAmount(amount);

        if (validation.isValid()) {
            paymentEntity.setStatus("PENDING");

            PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
            paymentHistoryStatusService.updatePaymentHistoryStatus(savedPayment.getId(), savedPayment.getStatus(), "В обработке");
            // Отправляем событие в Kafka
            PaymentEvent event = new PaymentEvent(
                    savedPayment.getId(),
                    savedPayment.getFromAccount(),
                    savedPayment.getToAccount(),
                    savedPayment.getAmount(),
                    savedPayment.getCurrency()
            );
            paymentEventProducer.sendPaymentEvent(event);

            return savedPayment;
        }
        // 5. Если ошибка ДЛЯ БД - сохраняем в БД с ошибкой
        if (!validation.isValid()) {
            paymentEntity.setStatus("FAILED");
            paymentEntity.setDescription(validation.getErrorMessage());

            return paymentRepository.save(paymentEntity);
        }

        // 6. Если все ок - сохраняем как "в обработке"
        paymentEntity.setStatus("PENDING");
        return paymentRepository.save(paymentEntity);
    }
    @GetMapping("/simple-test")
    public String simpleTest() {
        return "Simple test works! " ;
    }
}