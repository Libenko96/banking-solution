package com.solution.service;

import com.solution.dto.AccountDTO;
import com.solution.entity.Account;
import com.solution.exception.AccountNotFoundException;
import com.solution.mapper.AccountMapper;
import com.solution.repository.AccountRepository;
import com.solution.validator.AccountValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.solution.exception.AccountNotFoundException.NOT_FOUND_BY_ACCOUNT_NUMBER;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountValidator accountValidator;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountMapper accountMapper, AccountValidator accountValidator,
                              AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountValidator = accountValidator;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createAccount(BigDecimal initialBalance) {
        accountValidator.validateInitialBalance(initialBalance);
        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setBalance(initialBalance);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO getAccountDetails(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(String.format(NOT_FOUND_BY_ACCOUNT_NUMBER, accountNumber)));
        return accountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountMapper.toDTOs(accountRepository.findAll());
    }
}
