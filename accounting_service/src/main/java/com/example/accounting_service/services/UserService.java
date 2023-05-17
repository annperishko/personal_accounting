package com.example.accounting_service.services;

import com.example.accounting_service.dao.UsersRepo;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService
{
    private static  final Logger LOGGER = LoggerFactory.getLogger(AccountingService.class);
    public final UsersRepo usersRepo;

    public UserService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public UserDto getUserByEmail(String email) {
        try
        {
            User user = usersRepo.findUserByEmail(email);
            return user.mapToDto();

        }
        catch (RuntimeException e)
        {
            LOGGER.debug("Can`t find user");
            return null;
        }
    }
    public boolean createUser(UserDto userDto)
    {
        if (userDto != null)
        {
            User newUser = new User();
            newUser.setEmail(userDto.getEmail());
            newUser.setAccount(userDto.getAccount());

            usersRepo.save(newUser);
            return true;
        }
        else
            return false;
    }

    @Transactional
    public void deleteUserById(Integer userId)
    {
        usersRepo.deleteById(userId);
    }

}
