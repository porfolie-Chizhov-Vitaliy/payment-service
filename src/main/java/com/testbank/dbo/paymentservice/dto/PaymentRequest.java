package com.testbank.dbo.paymentservice.dto;

public class PaymentRequest {
    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String currency;

    // Геттеры и сеттеры
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}