package com.testbank.dbo.paymentservice.dto;

public class PaymentResult {

    private String status;
    private Long paymentId;
    private String message;
    public PaymentResult() {}

    public PaymentResult(String status, Long paymentId, String message, String errorType) {
        this.status = status;
        this.paymentId = paymentId;
        this.message = message;
        this.errorType = errorType;
    }
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String errorType;    // для ошибок



}
