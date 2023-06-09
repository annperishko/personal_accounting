package com.example.reporting_service.kafka;

import com.example.accounting_service.dto.TransactionDto;
import com.example.reporting_service.documents.Transaction;
import com.example.reporting_service.servises.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final TransactionService transactionService;

    public KafkaConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "test")
    public void consume(TransactionDto transactionDto) {
        Transaction transaction = new Transaction(
                transactionDto.getTransactionType(),
                transactionDto.getDescription(),
                transactionDto.getCategory(),
                transactionDto.getAmount(),
                transactionDto.getTransactionDate(),
                transactionDto.getUserId());
        this.transactionService.save(transaction);
    }
}
