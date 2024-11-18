package com.solution.validator;

import com.solution.entity.Account;

import java.math.BigDecimal;

public interface TransactionValidator {
    void validatePositiveAmount(BigDecimal amount);
    void validateSufficientFunds(Account account, BigDecimal amount);
}
