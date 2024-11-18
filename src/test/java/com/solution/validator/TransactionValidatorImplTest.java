package com.solution.validator;

import com.solution.entity.Account;
import com.solution.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionValidatorImplTest {

    @Autowired
    private TransactionValidator transactionValidator;

    @Test
    void shouldThrowValidationExceptionWhenAmountIsZeroOrNegative() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        BigDecimal negativeAmount = new BigDecimal("-100");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionValidator.validatePositiveAmount(zeroAmount);
        });
        assertEquals("Amount must be positive", exception.getMessage());

        exception = assertThrows(ValidationException.class, () -> {
            transactionValidator.validatePositiveAmount(negativeAmount);
        });
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationExceptionWhenAmountIsPositive() {
        BigDecimal positiveAmount = new BigDecimal("100");

        assertDoesNotThrow(() -> transactionValidator.validatePositiveAmount(positiveAmount));
    }

    @Test
    void shouldThrowValidationExceptionWhenInsufficientFunds() {
        Account account = new Account();
        account.setBalance(new BigDecimal("50"));
        account.setAccountNumber("12345");

        BigDecimal amountToWithdraw = new BigDecimal("100");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionValidator.validateSufficientFunds(account, amountToWithdraw);
        });
        assertEquals("Insufficient funds in account: 12345", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationExceptionWhenSufficientFunds() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        account.setAccountNumber("12345");

        BigDecimal amountToWithdraw = new BigDecimal("50");

        assertDoesNotThrow(() -> transactionValidator.validateSufficientFunds(account, amountToWithdraw));
    }

}
