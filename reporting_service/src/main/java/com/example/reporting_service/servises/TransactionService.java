package com.example.reporting_service.servises;

import com.example.accounting_service.kafka.KafkaProducer;
import com.example.reporting_service.documents.Transaction;
import com.example.reporting_service.repo.TransactionsRepository;
import com.example.reporting_service.utils.DateRangeDto;
import com.example.reporting_service.utils.ElasticSearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService
{
    private static  final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String USER_ID_FIELD = "userId";
    private static final String DATE_FIELD = "transactionDate";
    private final TransactionsRepository repo;
    private final RestHighLevelClient client;

    @Value("${spring.elasticsearch.account_transactions.index}")
    private String index;

    public TransactionService(TransactionsRepository repo, RestHighLevelClient client)
    {
        this.repo = repo;
        this.client = client;
    }

    public void save(Transaction transaction)
    {
        repo.save(transaction);
        LOGGER.debug("saved transaction");
    }

    public List<Transaction> findAllTransactions()
    {
        LOGGER.debug("find all transactions");
        return repo.findAll();
    }
    public List<Transaction> findAllByUserId(Integer userId)
    {
        LOGGER.debug("find all transactions by user id");
        return repo.findAllByUserId(userId);
    }

    public List<Transaction> findAllByCategory(Integer userId, String category)
    {
        LOGGER.debug("find all transactions by user id and category");
        return repo.findAllByUserIdAndCategory(userId, category);
    }

    public List<Transaction> findAllByUserIdAndTransactionType(Integer userId, String transactionType)
    {
        LOGGER.debug("find all transactions by user id and transactionType");
        return repo.findAllByUserIdAndTransactionType(userId, transactionType);
    }

    public List<Transaction> findByUserIdAndDateRange(DateRangeDto dateRangeDto, Integer userId)
    {
        MAPPER.registerModule(new JavaTimeModule());
        SearchRequest request = ElasticSearchUtil.buildSearchRequest(index, DATE_FIELD, dateRangeDto, USER_ID_FIELD, userId);
        if (request == null)
        {
            return Collections.emptyList();
        }
        try
        {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Transaction> transactions = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                transactions.add(
                        MAPPER.readValue(hit.getSourceAsString(), Transaction.class)
                );
            }
            LOGGER.debug("return transactions list by user id and date range");
            return transactions;
        }
        catch (Exception e)
        {
            LOGGER.debug("unexpected error, return empty list");
            return Collections.emptyList();
        }
    }

}


