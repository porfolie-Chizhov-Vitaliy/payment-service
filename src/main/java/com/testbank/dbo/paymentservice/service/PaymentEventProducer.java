package com.testbank.dbo.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbank.dbo.paymentservice.dto.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {
    private static final String TOPIC = "balance-checks";
    private final KafkaTemplate<String, String> kafkaTemplate; // ← Измени на String
    private final ObjectMapper objectMapper;

    public PaymentEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPaymentEvent(PaymentEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.getPaymentId().toString(), eventJson);
            System.out.println("Отправлено событие в Kafka топик balance-checks: " + event.getPaymentId());
        } catch (JsonProcessingException e) {
            System.out.println("ERROR" + e);
        }
    }

}
