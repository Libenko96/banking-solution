package com.solution.controller;

import com.solution.dto.AccountDTO;
import com.solution.dto.TransactionRequest;
import com.solution.dto.TransferRequest;
import com.solution.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestBody TransactionRequest request) {
        AccountDTO account = transactionService.deposit(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestBody TransactionRequest request) {
        AccountDTO account = transactionService.withdraw(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok(account);
    }
}
