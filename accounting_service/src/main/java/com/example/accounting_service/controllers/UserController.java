package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.exceptions.UserNotFoundException;
import com.example.accounting_service.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
@RestController
@AllArgsConstructor
public class UserController
{

    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) throws UserNotFoundException
    {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return ResponseEntity.ok(userService.getAll());
    }
    @PostMapping()
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto)
    {
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) throws UserNotFoundException {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
