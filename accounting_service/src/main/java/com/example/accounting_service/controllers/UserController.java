package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController
{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email)
    {
        UserDto userDto = userService.getUserByEmail(email);
        if(userDto != null)
        {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto)
    {

        if(userService.createUser(userDto))
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
        userService.deleteUserById(userId);
        return ResponseEntity.ok("Deleted user");
    }
}
