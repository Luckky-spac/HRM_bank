package com.hrm.temp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrm.temp.dto.TransactionDTO;
import com.hrm.temp.entity.Transaction;
import com.hrm.temp.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // GET all transactions
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTransactions(@RequestParam(required = false) Boolean withRisk) {
        List<?> transactions;

        if (Boolean.TRUE.equals(withRisk)) {
            transactions = service.getAllTransactionsWithRisk();
        } else {
            transactions = service.getAllTransactions();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", transactions);
        return ResponseEntity.ok(response);
    }

    // GET transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        return service.getTransactionById(id)
            .map(txn -> {
                Map<String, Object> response = new HashMap<>();
                response.put("data", txn);
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Transaction not found")));
    }

    // CREATE transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO dto) {
        Transaction txn = service.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(txn);
    }

    // UPDATE transaction
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        try {
            Transaction updated = service.updateTransaction(id, dto);
            Map<String, Object> response = new HashMap<>();
            response.put("data", updated);
            response.put("message", "Transaction updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            service.deleteTransaction(id);
            return ResponseEntity.ok(Map.of("message", "Transaction deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
