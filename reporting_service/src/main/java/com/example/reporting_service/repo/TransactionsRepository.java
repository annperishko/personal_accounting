package com.example.reporting_service.repo;

import com.example.reporting_service.documents.Transaction;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsRepository extends ElasticsearchRepository<Transaction, String> {
    List<Transaction> findAll();

    List<Transaction> findAllByUserId(Integer userId);

    List<Transaction> findAllByUserIdAndTransactionType(Integer userId, String transactionType);

    List<Transaction> findAllByUserIdAndCategory(Integer userId, String category);

    @Query("{\n" +
            "  \"bool\": {\n" +
            "    \"must\": [\n" +
            "      {\n" +
            "        \"range\": {\n" +
            "          \"transactionDate\": {\n" +
            "            \"gte\": \"?0\",\n" +
            "            \"lte\": \"?1\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"filter\": [\n" +
            "      {\n" +
            "        \"term\": {\n" +
            "          \"userId\": \"?2\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}")
    List<Transaction> findAllByUserIdAndTransactionDate(@Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("userId") Integer userId);

}
