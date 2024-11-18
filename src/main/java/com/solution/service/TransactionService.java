package com.solution.service;

import com.solution.dto.AccountDTO;

import java.math.BigDecimal;

public interface TransactionService {

    AccountDTO deposit(String accountNumber, BigDecimal amount);

    AccountDTO withdraw(String accountNumber, BigDecimal amount);

    void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);
}
