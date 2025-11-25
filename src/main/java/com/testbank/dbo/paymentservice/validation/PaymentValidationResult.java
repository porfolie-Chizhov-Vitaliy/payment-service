package com.testbank.dbo.paymentservice.validation;

public class PaymentValidationResult {

    private final boolean valid;
    private final String errorMessage;
    private final boolean saveToDatabase;

    public PaymentValidationResult(boolean valid, String errorMessage, boolean saveToDatabase) {
        this.valid = valid;
        this.errorMessage = errorMessage;
        this.saveToDatabase = saveToDatabase;
    }

    public static PaymentValidationResult success() {
        return new PaymentValidationResult(true, null, true);
    }

    public static PaymentValidationResult failed(String errorMessage, boolean saveToDatabase) {
        return new PaymentValidationResult(false, errorMessage, saveToDatabase);
    }

    // Геттеры
    public boolean isValid() { return valid; }
    public String getErrorMessage() { return errorMessage; }
    public boolean shouldSaveToDatabase() { return saveToDatabase; }
}
