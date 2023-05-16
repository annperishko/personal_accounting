package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.services.AccountingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/account")
@RestController
public class AccountController
{
    private final AccountingService accountingService;

    public AccountController(AccountingService accountingService)
    {
        this.accountingService = accountingService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email)
    {
        UserDto userDto = accountingService.getUserByEmail(email);
        if(userDto != null)
        {
            return ResponseEntity.ok(accountingService.getUserByEmail(email));
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/{email}/save")
    public ResponseEntity<String> saveNewTransaction(@PathVariable String email, @RequestBody TransactionDto transactionDto)
    {
        UserDto userDto = accountingService.getUserByEmail(email);
        if(userDto != null)
        {
            if(accountingService.saveTransaction(email, transactionDto))
            {
                return ResponseEntity.ok( "saved transaction");
            }
            else
            {
                return new ResponseEntity<>("can`t save transaction", HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity<>("User doesn`t exist", HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto)
    {

        if(accountingService.createUser(userDto))
        {
            return ResponseEntity.ok("User created");
        }
        else
        {
            return new ResponseEntity<>("Can`t create user", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable int userId) {
        accountingService.deleteUserById(userId);
        return ResponseEntity.ok("Deleted user");
    }
}
