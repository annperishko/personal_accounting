package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.services.AccountingService;
import com.example.accounting_service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/account")
@RestController
public class AccountController
{
    private final AccountingService accountingService;
    private final UserService userService;

    public AccountController(AccountingService accountingService, UserService userService)
    {
        this.accountingService = accountingService;
        this.userService = userService;
    }

    @PostMapping("/{email}/save")
    public ResponseEntity<String> saveNewTransaction(@PathVariable String email, @RequestBody TransactionDto transactionDto)
    {
        UserDto userDto = userService.getUserByEmail(email);
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

}
