package com.example.accounting_service.entities;

import com.example.accounting_service.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "surname")
    private String surname;
    @Column(nullable = false, name = "account")
    private BigDecimal account;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "role")
    private Integer role;
    @Column(nullable = false, name = "auth_provider")
    private String authProvider;

    public UserDto mapToDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail(this.getEmail());
        userDto.setName(this.getName());
        userDto.setSurname(this.getSurname());
        userDto.setAccount(this.getAccount());
        userDto.setPassword(this.getPassword());
        userDto.setRole(this.getRole());
        userDto.setAuthProvider(this.getAuthProvider());
        return userDto;
    }
}
