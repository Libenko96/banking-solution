package com.solution.service;

import com.solution.dto.AccountDTO;
import com.solution.entity.Account;
import com.solution.exception.AccountNotFoundException;
import com.solution.mapper.AccountMapper;
import com.solution.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountMapper accountMapper;

    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setBalance(BigDecimal.valueOf(1000));

        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());
    }

    @Test
    void createAccount_shouldReturnCreatedAccountDTO() {
        BigDecimal initialBalance = BigDecimal.valueOf(500);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);

        AccountDTO result = accountService.createAccount(initialBalance);

        assertNotNull(result);
        assertEquals(accountDTO.getAccountNumber(), result.getAccountNumber());
        assertEquals(accountDTO.getBalance(), result.getBalance());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertEquals(initialBalance, accountCaptor.getValue().getBalance());
    }

    @Test
    void getAccountDetails_shouldReturnAccountDTO() {
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        AccountDTO result = accountService.getAccountDetails(account.getAccountNumber());

        assertNotNull(result);
        assertEquals(accountDTO.getAccountNumber(), result.getAccountNumber());
        assertEquals(accountDTO.getBalance(), result.getBalance());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void getAccountDetails_shouldThrowExceptionWhenAccountNotFound() {
        String invalidAccountNumber = "invalid_account";
        when(accountRepository.findByAccountNumber(invalidAccountNumber)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountDetails(invalidAccountNumber));
        assertEquals("Account not found by this accountNumber invalid_account", exception.getMessage());

        verify(accountRepository).findByAccountNumber(invalidAccountNumber);
    }

    @Test
    void getAllAccounts_shouldReturnListOfAccountDTOs() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.toDTOs(List.of(account))).thenReturn(List.of(accountDTO));

        List<AccountDTO> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountDTO.getAccountNumber(), result.get(0).getAccountNumber());

        verify(accountRepository).findAll();
    }
}
