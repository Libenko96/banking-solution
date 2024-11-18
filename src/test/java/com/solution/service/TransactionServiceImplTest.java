package com.solution.service;

import com.solution.dto.AccountDTO;
import com.solution.entity.Account;
import com.solution.exception.ValidationException;
import com.solution.mapper.AccountMapper;
import com.solution.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountMapper accountMapper;

    @Autowired
    private TransactionService transactionService;

    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setBalance(BigDecimal.valueOf(1000));

        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());
    }

    @Test
    void deposit_ValidAmount_ShouldIncreaseBalance() {
        BigDecimal depositAmount = BigDecimal.valueOf(200);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenAnswer(invocation -> {
            Account updatedAccount = invocation.getArgument(0);
            AccountDTO updatedDTO = new AccountDTO();
            updatedDTO.setAccountNumber(updatedAccount.getAccountNumber());
            updatedDTO.setBalance(updatedAccount.getBalance());
            return updatedDTO;
        });

        AccountDTO result = transactionService.deposit(account.getAccountNumber(), depositAmount);

        assertEquals(account.getBalance(), result.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void deposit_NonPositiveAmount_ShouldThrowException() {
        BigDecimal depositAmount = BigDecimal.valueOf(-100);

        assertThrows(ValidationException.class, () ->
                transactionService.deposit(account.getAccountNumber(), depositAmount));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void withdraw_ValidAmount_ShouldDecreaseBalance() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(300);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenAnswer(invocation -> {
            Account updatedAccount = invocation.getArgument(0);
            AccountDTO updatedDTO = new AccountDTO();
            updatedDTO.setAccountNumber(updatedAccount.getAccountNumber());
            updatedDTO.setBalance(updatedAccount.getBalance());
            return updatedDTO;
        });

        AccountDTO result = transactionService.withdraw(account.getAccountNumber(), withdrawAmount);

        assertEquals(account.getBalance(), result.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_InsufficientBalance_ShouldThrowException() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(1500);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(ValidationException.class, () ->
                transactionService.withdraw(account.getAccountNumber(), withdrawAmount));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void transfer_ValidAmount_ShouldTransferFunds() {
        Account fromAccount = new Account();
        fromAccount.setAccountNumber(UUID.randomUUID().toString());
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        Account toAccount = new Account();
        toAccount.setAccountNumber(UUID.randomUUID().toString());
        toAccount.setBalance(BigDecimal.valueOf(500));

        BigDecimal transferAmount = BigDecimal.valueOf(200);

        when(accountRepository.findByAccountNumber(fromAccount.getAccountNumber())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(Optional.of(toAccount));

        transactionService.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), transferAmount);

        assertEquals(BigDecimal.valueOf(800), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(700), toAccount.getBalance());
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void transfer_InsufficientBalance_ShouldThrowException() {
        Account fromAccount = new Account();
        fromAccount.setAccountNumber(UUID.randomUUID().toString());
        fromAccount.setBalance(BigDecimal.valueOf(100));

        Account toAccount = new Account();
        toAccount.setAccountNumber(UUID.randomUUID().toString());
        toAccount.setBalance(BigDecimal.valueOf(500));

        BigDecimal transferAmount = BigDecimal.valueOf(200);

        when(accountRepository.findByAccountNumber(fromAccount.getAccountNumber())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(Optional.of(toAccount));

        assertThrows(ValidationException.class, () ->
                transactionService.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), transferAmount));

        verify(accountRepository, never()).save(fromAccount);
        verify(accountRepository, never()).save(toAccount);
    }

    @Test
    void withdraw_NegativeAmount_ShouldThrowException() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(-100);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> transactionService.withdraw(account.getAccountNumber(), withdrawAmount)
        );

        assertEquals("Amount must be positive", thrown.getMessage());
    }

    @Test
    void transfer_NegativeAmount_ShouldThrowException() {
        BigDecimal transferAmount = BigDecimal.valueOf(-100);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> transactionService.transfer(new Account().getAccountNumber(), new Account().getAccountNumber(), transferAmount)
        );

        assertEquals("Amount must be positive", thrown.getMessage());
    }
}
