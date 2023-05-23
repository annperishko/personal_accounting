package com.example.accounting_service.services;

import com.example.accounting_service.repositories.UsersRepo;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    public final UsersRepo usersRepo;

    public UserDto getUserByEmail(String email) throws UserNotFoundException
    {

        User user = usersRepo.findUserByEmail(email);
        if (user != null)
        {
            return user.mapToDto();
        }
        else
        {
            throw new UserNotFoundException("User not found");
        }

    }

    public List<UserDto> getAll()
    {
        return usersRepo.findAll().stream().map(User::mapToDto).collect(Collectors.toList());
    }

    public void createUser(UserDto userDto)
    {
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setAccount(userDto.getAccount());
        usersRepo.save(newUser);
    }

    public void deleteUserById(Integer userId) throws UserNotFoundException
    {
        if (usersRepo.findById(userId).isPresent())
        {
            usersRepo.deleteById(userId);
        }
        else throw new UserNotFoundException("User not found");
    }

}
