package com.example.accounting_service.services;

import com.example.accounting_service.enums.TransactionType;
import com.example.accounting_service.exceptions.NotCompatibleCategory;
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
    private final UsersRepo usersRepo;
    private final KafkaProducer kafkaProducer;
    private final static TransactionType EARNING_TYPE = TransactionType.EARNING;
    private final static TransactionType EXPENSE_TYPE = TransactionType.EXPENSE;

    public void saveTransaction(String email, TransactionDto transactionDto) {
        User user = usersRepo.findUserByEmail(email);
        if (user != null) {
            if(transactionDto.getTransactionType() == transactionDto.getCategory().getType()) {
                transactionDto.setUserId(user.getId());
                if (transactionDto.getTransactionType() == EARNING_TYPE) {
                    user.setAccount(user.getAccount().add(transactionDto.getAmount()));
                } else if (transactionDto.getTransactionType() == EXPENSE_TYPE) {
                    user.setAccount(user.getAccount().subtract(transactionDto.getAmount()));
                }
                usersRepo.save(user);
                kafkaProducer.sendMessage(transactionDto);
            } else {
                throw new NotCompatibleCategory("Type of category " + transactionDto.getCategory() + " is not compatible with transaction type " + transactionDto.getTransactionType());
            }

        } else {
            throw new UserNotFoundException("User not found");
        }

    }

}
