package com.solution.validator;

import com.solution.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AccountValidatorImplTest {
    @Autowired
    private AccountValidator accountValidator;

    @Test
    void shouldThrowValidationExceptionWhenInitialBalanceIsNegative() {
        BigDecimal negativeBalance = new BigDecimal("-100");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            accountValidator.validateInitialBalance(negativeBalance);
        });

        assertEquals("Initial balance must be non-negative", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationExceptionWhenInitialBalanceIsZero() {
        BigDecimal zeroBalance = BigDecimal.ZERO;

        assertDoesNotThrow(() -> accountValidator.validateInitialBalance(zeroBalance));
    }

    @Test
    void shouldNotThrowValidationExceptionWhenInitialBalanceIsPositive() {
        BigDecimal positiveBalance = new BigDecimal("100");

        assertDoesNotThrow(() -> accountValidator.validateInitialBalance(positiveBalance));
    }
}
