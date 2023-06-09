package com.example.reporting_service.documents;

import com.example.accounting_service.enums.Category;
import com.example.accounting_service.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "account_transactions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Field(type = FieldType.Text, name = "id")
    private String id;
    @Field(type = FieldType.Text, name = "transactionType")
    private TransactionType transactionType;
    @Field(type = FieldType.Text, name = "description")
    private String description;
    @Field(type = FieldType.Text, name = "category")
    private Category category;
    @Field(type = FieldType.Double, name = "amount")
    private BigDecimal amount;
    @Field(type = FieldType.Date, name = "transactionDate", format = DateFormat.date, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    @Field(type = FieldType.Integer)
    private Integer userId;

    public Transaction(TransactionType transactionType, String description,
                       Category category, BigDecimal amount, LocalDate transactionDate, Integer userId) {
        this.transactionType = transactionType;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.userId = userId;
    }
}