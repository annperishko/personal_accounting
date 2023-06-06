package com.example.accounting_service.entities;

import com.example.accounting_service.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "account")
    private BigDecimal account;

    public UserDto mapToDto() {
        UserDto userDto = new UserDto();
        userDto.setId(this.getId());
        userDto.setEmail(this.getEmail());
        userDto.setAccount(this.getAccount());
        return userDto;
    }
}
