package com.solution.validator;

import java.math.BigDecimal;

public interface AccountValidator {
    void validateInitialBalance(BigDecimal initialBalance);
}
