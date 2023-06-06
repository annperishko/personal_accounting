package com.example.accounting_service.dto;

import com.example.accounting_service.enums.Category;
import com.example.accounting_service.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {
    @NotNull(message = "Type can`t be null")
    private TransactionType transactionType;
    private String description;
    @NotNull(message = "Category can`t be null")
    private Category category;
    @NotNull(message = "Amount can`t be null")
    private BigDecimal amount;
    @NotNull(message = "Date can`t be null")
    @PastOrPresent(message = "Future date is not acceptable")
    private LocalDate transactionDate;
    private Integer userId;
}
