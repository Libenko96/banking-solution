package com.solution.validator;

import com.solution.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountValidatorImpl implements AccountValidator {

    public void validateInitialBalance(BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Initial balance must be non-negative");
        }
    }
}
