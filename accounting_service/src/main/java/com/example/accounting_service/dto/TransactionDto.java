package com.example.accounting_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto
{
    private String transactionType;
    private String description;
    private String category;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private Integer userId;

}
