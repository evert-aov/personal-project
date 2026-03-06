package com.uagrm.schedule_assignment.finance.controller;

import com.uagrm.schedule_assignment.finance.dto.TransactionRequestDto;
import com.uagrm.schedule_assignment.finance.dto.TransactionResponseDto;
import com.uagrm.schedule_assignment.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/finance/transactions")
@RequiredArgsConstructor
@Tag(name = "Finance Transactions", description = "Endpoints para la gestión de transacciones financieras")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Registrar una nueva transacción")
    public ResponseEntity<TransactionResponseDto> addTransaction(@RequestBody TransactionRequestDto requestDto) {
        return new ResponseEntity<>(transactionService.addMyTransaction(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getMyTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<TransactionResponseDto>> getAllMyTransactions() {
        return ResponseEntity.ok(transactionService.getAllMyTransactions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequestDto requestDto) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/debt/loan")
    @Operation(summary = "Obtener el balance total de deudas/préstamos")
    public BigDecimal totalDebt() {
        return transactionService.totalDebt();
    }
}
