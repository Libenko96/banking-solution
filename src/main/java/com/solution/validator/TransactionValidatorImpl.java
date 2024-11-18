package com.solution.validator;

import com.solution.entity.Account;
import com.solution.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidatorImpl implements TransactionValidator {

    @Override
    public void validatePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }
    }

    @Override
    public void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new ValidationException("Insufficient funds in account: " + account.getAccountNumber());
        }
    }
}
