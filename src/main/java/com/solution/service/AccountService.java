package com.solution.service;

import com.solution.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDTO createAccount(BigDecimal initialBalance);

    AccountDTO getAccountDetails(String accountNumber);

    List<AccountDTO> getAllAccounts();
}
