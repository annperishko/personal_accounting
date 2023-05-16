package com.example.accounting_service.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto
{
    private Integer id;
    private String email;
    private BigDecimal account;

}
