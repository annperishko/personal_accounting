package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.exceptions.UserNotFoundException;
import com.example.accounting_service.services.AccountingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/account")
@RestController
@AllArgsConstructor
@Slf4j
public class AccountController
{
    private final AccountingService accountingService;

    @PostMapping("/{email}")
    public ResponseEntity<Void> saveNewTransaction(@Valid @RequestBody TransactionDto transactionDto, @PathVariable String email)
    {
        try
        {
            accountingService.saveTransaction(email, transactionDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            log.error("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Throwable e)
        {
            log.error("Can`t save transaction");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

}
