package com.solution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.dto.AccountDTO;
import com.solution.dto.CreateAccountRequest;
import com.solution.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setInitialBalance(BigDecimal.valueOf(500));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(UUID.randomUUID().toString());
        accountDTO.setBalance(BigDecimal.valueOf(500));

        when(accountService.createAccount(request.getInitialBalance())).thenReturn(accountDTO);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(accountDTO.getAccountNumber()))
                .andExpect(jsonPath("$.balance").value(accountDTO.getBalance().intValue()));
    }

    @Test
    void getAccountDetails_ShouldReturnAccount() throws Exception {
        String accountNumber = UUID.randomUUID().toString();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setBalance(BigDecimal.valueOf(1000));

        when(accountService.getAccountDetails(accountNumber)).thenReturn(accountDTO);

        mockMvc.perform(get("/api/accounts/{accountNumber}", accountNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.balance").value(accountDTO.getBalance().intValue()));
    }

    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() throws Exception {
        AccountDTO account1 = new AccountDTO();
        account1.setAccountNumber(UUID.randomUUID().toString());
        account1.setBalance(BigDecimal.valueOf(1000));

        AccountDTO account2 = new AccountDTO();
        account2.setAccountNumber(UUID.randomUUID().toString());
        account2.setBalance(BigDecimal.valueOf(500));

        List<AccountDTO> accounts = Arrays.asList(account1, account2);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(accounts.size()))
                .andExpect(jsonPath("$[0].accountNumber").value(account1.getAccountNumber()))
                .andExpect(jsonPath("$[0].balance").value(account1.getBalance().intValue()))
                .andExpect(jsonPath("$[1].accountNumber").value(account2.getAccountNumber()))
                .andExpect(jsonPath("$[1].balance").value(account2.getBalance().intValue()));
    }
}
