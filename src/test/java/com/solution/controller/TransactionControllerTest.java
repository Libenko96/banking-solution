package com.solution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.dto.AccountDTO;
import com.solution.dto.TransactionRequest;
import com.solution.dto.TransferRequest;
import com.solution.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deposit_shouldReturnAccountDTO() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAccountNumber("123456789");
        request.setAmount(BigDecimal.valueOf(500));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        accountDTO.setBalance(BigDecimal.valueOf(1500));

        when(transactionService.deposit("123456789", BigDecimal.valueOf(500))).thenReturn(accountDTO);

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456789"))
                .andExpect(jsonPath("$.balance").value(1500));
    }

    @Test
    void withdraw_shouldReturnAccountDTO() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAccountNumber("123456789");
        request.setAmount(BigDecimal.valueOf(500));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        accountDTO.setBalance(BigDecimal.valueOf(1000));

        when(transactionService.withdraw("123456789", BigDecimal.valueOf(500))).thenReturn(accountDTO);

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456789"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void transfer_shouldReturnNoContent() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("123456789");
        request.setToAccount("987654321");
        request.setAmount(BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}
