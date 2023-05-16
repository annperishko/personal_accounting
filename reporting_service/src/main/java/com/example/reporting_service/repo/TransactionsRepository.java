package com.example.reporting_service.repo;

import com.example.reporting_service.documents.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface TransactionsRepository extends ElasticsearchRepository<Transaction, Long>
{
    List<Transaction> findAll();
    List<Transaction> findAllByUserId(Integer userId);
    List<Transaction> findAllByUserIdAndTransactionType(Integer userId, String transactionType);
    List<Transaction> findAllByUserIdAndCategory(Integer userId, String category);

}
