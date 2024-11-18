package com.solution.service;

import com.solution.dto.AccountDTO;
import com.solution.entity.Account;
import com.solution.exception.AccountNotFoundException;
import com.solution.mapper.AccountMapper;
import com.solution.repository.AccountRepository;
import com.solution.validator.TransactionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.solution.exception.AccountNotFoundException.NOT_FOUND_BY_ACCOUNT_NUMBER;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TransactionValidator transactionValidator;

    public TransactionServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper,
                                  TransactionValidator transactionValidator) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionValidator = transactionValidator;
    }

    @Override
    @Transactional
    public AccountDTO deposit(String accountNumber, BigDecimal amount) {
        transactionValidator.validatePositiveAmount(amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(NOT_FOUND_BY_ACCOUNT_NUMBER, accountNumber)));

        account.setBalance(account.getBalance().add(amount));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountDTO withdraw(String accountNumber, BigDecimal amount) {
        transactionValidator.validatePositiveAmount(amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(NOT_FOUND_BY_ACCOUNT_NUMBER, accountNumber)));

        transactionValidator.validateSufficientFunds(account, amount);

        account.setBalance(account.getBalance().subtract(amount));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        transactionValidator.validatePositiveAmount(amount);

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(NOT_FOUND_BY_ACCOUNT_NUMBER, fromAccountNumber)));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(NOT_FOUND_BY_ACCOUNT_NUMBER, fromAccountNumber)));

        transactionValidator.validateSufficientFunds(fromAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
