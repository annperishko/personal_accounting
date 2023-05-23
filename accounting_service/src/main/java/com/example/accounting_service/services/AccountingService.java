package com.example.accounting_service.services;

import com.example.accounting_service.repositories.UsersRepo;
import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.exceptions.UserNotFoundException;
import com.example.accounting_service.kafka.KafkaProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountingService {
    public final UsersRepo usersRepo;
    private final KafkaProducer kafkaProducer;

    public void saveTransaction(String email, TransactionDto transactionDto) throws UserNotFoundException
    {
        User user = usersRepo.findUserByEmail(email);
        if (user != null)
        {
            transactionDto.setUserId(user.getId());
            if (transactionDto.getTransactionType().getType() == 0 && transactionDto.getCategory().getType() == 0)
            {
                user.setAccount(user.getAccount().add(transactionDto.getAmount()));
            }
            else if (transactionDto.getTransactionType().getType() == 1 && transactionDto.getCategory().getType() == 1)
            {
                user.setAccount(user.getAccount().subtract(transactionDto.getAmount()));
            }

            usersRepo.save(user);
            kafkaProducer.sendMessage(transactionDto);
        }
        else
            throw new UserNotFoundException("User doesn`t exist");

    }

}
