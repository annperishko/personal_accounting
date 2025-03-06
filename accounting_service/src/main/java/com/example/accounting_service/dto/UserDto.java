package com.example.accounting_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Can`t be empty")
    private String email;
    private String name;
    private String surname;
    private BigDecimal account;
    @NotBlank(message = "Can`t be empty")
    private String password;
    private Integer role;
    private String authProvider;

}
