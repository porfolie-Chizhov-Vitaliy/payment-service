package com.testbank.dbo.paymentservice.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;
@Component
public class PaymentValidator {

    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^\\d{20}$");

    public PaymentValidationResult  validatePayment(String fromAccount, String toAccount, BigDecimal amount) {

        // 1. Неверные реквизиты отправителя - НЕ сохраняем в БД
        if (!isValidAccount(fromAccount)) {
            return PaymentValidationResult.failed("Неверный номер счета отправителя", false);
        }

        // 2. Перевод самому себе - НЕ сохраняем в БД
        if (fromAccount.equals(toAccount)) {
            return PaymentValidationResult .failed("Нельзя переводить самому себе", false);
        }

        // 3. Неверная сумма - НЕ сохраняем в БД
        if (amount == null || amount.compareTo(BigDecimal.ZERO)<=0) {
            return PaymentValidationResult .failed("Неверная сумма платежа", false);
        }

        // 4. Неверные реквизиты получателя - СОХРАНЯЕМ в БД
        if (!isValidAccount(toAccount)) {
            return PaymentValidationResult .failed("Неверный номер счета получателя", true);
        }

        return PaymentValidationResult .success();
    }

    private boolean isValidAccount(String account) {
        return account != null && ACCOUNT_PATTERN.matcher(account).matches();
    }


}
