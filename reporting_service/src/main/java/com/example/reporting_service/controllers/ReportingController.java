package com.example.reporting_service.controllers;

import com.example.reporting_service.documents.Transaction;
import com.example.reporting_service.servises.TransactionService;
import com.example.reporting_service.utils.DateRangeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportingController
{
    private final TransactionService transactionService;

    public ReportingController(TransactionService transactionService)
    {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> findAll()
    {
        return ResponseEntity.ok(transactionService.findAllTransactions());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> findAllByUserId(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(transactionService.findAllByUserId(userId));
    }

    @GetMapping("/{userId}/category/{category}")
    public ResponseEntity<List<Transaction>> findAllByCategory(@PathVariable Integer userId, @PathVariable String category)
    {
        return ResponseEntity.ok(transactionService.findAllByCategory(userId, category));
    }

    @GetMapping("/{userId}/type/{transactionType}")
    public ResponseEntity<List<Transaction>> findAllByType(@PathVariable Integer userId, @PathVariable String transactionType)
    {
        return ResponseEntity.ok(transactionService.findAllByUserIdAndTransactionType(userId, transactionType));
    }

    @GetMapping("/{userId}/search")
    public ResponseEntity<List<Transaction>> findAllByDateRange(
            @PathVariable Integer userId,
            @RequestBody DateRangeDto range)
    {
        return ResponseEntity.ok(transactionService.findByUserIdAndDateRange(range, userId));
    }

}
