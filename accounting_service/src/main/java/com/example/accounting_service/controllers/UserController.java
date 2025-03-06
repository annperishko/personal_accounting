package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto) {
        if (!userService.isUserEmailAlreadyUsed(userDto.getEmail()) && Strings.isNotBlank(userDto.getPassword())) {
            userService.createUser(userDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            log.error("User with email: " + userDto.getEmail() + " already exist");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
