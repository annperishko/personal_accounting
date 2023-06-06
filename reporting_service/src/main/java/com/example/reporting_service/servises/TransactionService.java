package com.example.reporting_service.servises;

import com.example.reporting_service.documents.Transaction;
import com.example.reporting_service.dto.DateRangeDto;
import com.example.reporting_service.repo.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionsRepository repo;

    public TransactionService(TransactionsRepository repo) {
        this.repo = repo;
    }

    public void save(Transaction transaction) {
        repo.save(transaction);
    }

    public List<Transaction> findAllTransactions() {
        return repo.findAll();
    }

    public List<Transaction> findAllByUserId(Integer userId) {
        return repo.findAllByUserId(userId);
    }

    public List<Transaction> findAllByCategory(Integer userId, String category) {
        return repo.findAllByUserIdAndCategory(userId, category);
    }

    public List<Transaction> findAllByUserIdAndTransactionType(Integer userId, String transactionType) {
        return repo.findAllByUserIdAndTransactionType(userId, transactionType);
    }

    public List<Transaction> findByDateRangeAndUserId(DateRangeDto dateRangeDto, Integer userId) {
        return repo.findAllByUserIdAndTransactionDate(dateRangeDto.getStartDate(), dateRangeDto.getEndDate(), userId);
    }

}


