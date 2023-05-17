package com.example.accounting_service.services;

import com.example.accounting_service.dao.UsersRepo;
import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.enums.TransactionType;
import com.example.accounting_service.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class AccountingService
{
    private static  final Logger LOGGER = LoggerFactory.getLogger(AccountingService.class);
    public final UsersRepo usersRepo;
    private final KafkaProducer kafkaProducer;

    public AccountingService(UsersRepo usersRepo, KafkaProducer kafkaProducer)
    {
        this.usersRepo = usersRepo;
        this.kafkaProducer = kafkaProducer;
    }


    @Transactional
    public boolean saveTransaction(String email, TransactionDto transactionDto)
    {
        try
        {
            User user = usersRepo.findUserByEmail(email);
            if(user != null)
            {
                transactionDto.setUserId(user.getId());
                if (Objects.equals(transactionDto.getTransactionType().toUpperCase(), TransactionType.EXPENSE.name()))
                {
                    user.setAccount(user.getAccount().subtract(transactionDto.getAmount()));
                }
                else if (Objects.equals(transactionDto.getTransactionType().toUpperCase(), TransactionType.EARNING.name()))
                {
                    user.setAccount(user.getAccount().add(transactionDto.getAmount()));
                }
                else
                {
                    LOGGER.debug("Can`t save transaction as such transactionType doesn`t exist");
                    return false;
                }
                kafkaProducer.sendMessage(transactionDto);
                return true;
            }
            else
                return false;
        }
        catch (RuntimeException e)
        {
            LOGGER.debug("Can`t save transaction");
            return false;
        }

    }

}
