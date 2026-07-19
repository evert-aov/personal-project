package com.uagrm.personal.finance.controller;

import com.uagrm.personal.finance.dto.TransactionRequestDto;
import com.uagrm.personal.finance.dto.TransactionResponseDto;
import com.uagrm.personal.finance.dto.UserTransactionSummaryDto;
import com.uagrm.personal.finance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {
    private final TransactionService transactionService;

    @GetMapping("/summary")
    public ResponseEntity<Iterable<UserTransactionSummaryDto>> getSummary() {
        return ResponseEntity.ok(transactionService.getUserSummaries());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<TransactionResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getPaymentsAndLoansByUser(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<TransactionResponseDto> createForUser(
            @PathVariable Long userId,
            @RequestBody TransactionRequestDto requestDto
    ) {
        return new ResponseEntity<>(transactionService.createForUser(userId, requestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAny(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
