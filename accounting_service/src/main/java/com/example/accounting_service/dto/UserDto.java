package com.example.accounting_service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotBlank(message = "Can`t be empty")
    private String email;
    @NotNull(message = "Can`t be null")
    private BigDecimal account;

}
