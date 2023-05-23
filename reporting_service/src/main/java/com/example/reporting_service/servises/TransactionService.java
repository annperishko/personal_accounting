package com.example.reporting_service.servises;

import com.example.reporting_service.documents.Transaction;
import com.example.reporting_service.dto.DateRangeDto;
import com.example.reporting_service.repo.TransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransactionService
{
    private final TransactionsRepository repo;

    public TransactionService(TransactionsRepository repo)
    {
        this.repo = repo;
    }

    public void save(Transaction transaction)
    {
        repo.save(transaction);
        log.debug("saved transaction");
    }

    public List<Transaction> findAllTransactions()
    {
        log.debug("find all transactions");
        return repo.findAll();
    }
    public List<Transaction> findAllByUserId(Integer userId)
    {
        log.debug("find all transactions by user id");
        return repo.findAllByUserId(userId);
    }

    public List<Transaction> findAllByCategory(Integer userId, String category)
    {
        log.debug("find all transactions by user id and category");
        return repo.findAllByUserIdAndCategory(userId, category);
    }

    public List<Transaction> findAllByUserIdAndTransactionType(Integer userId, String transactionType)
    {
        log.debug("find all transactions by user id and transactionType");
        return repo.findAllByUserIdAndTransactionType(userId, transactionType);
    }

    public List<Transaction> findByDateRangeAndUserId(DateRangeDto dateRangeDto, Integer userId)
    {
       return repo.findAllByUserIdAndTransactionDate(dateRangeDto.getStartDate(), dateRangeDto.getEndDate(), userId);
    }

}


