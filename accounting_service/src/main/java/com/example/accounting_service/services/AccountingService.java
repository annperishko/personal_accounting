package com.example.accounting_service.services;

import com.example.accounting_service.dto.TransactionDto;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.enums.TransactionType;
import com.example.accounting_service.exceptions.NotCompatibleCategory;
import com.example.accounting_service.exceptions.UserNotFoundException;
import com.example.accounting_service.kafka.KafkaProducer;
import com.example.accounting_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AccountingService {

    private final UserRepository usersRepo;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void saveTransaction(String email, TransactionDto transactionDto) {
        User currentUser = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (transactionDto.getTransactionType() == transactionDto.getCategory().getType()) {
            transactionDto.setUserId(currentUser.getId());

            if (transactionDto.getTransactionType() == TransactionType.EARNING) {
                currentUser.setAccount(currentUser.getAccount().add(transactionDto.getAmount()));
            } else if (transactionDto.getTransactionType() == TransactionType.EXPENSE) {
                currentUser.setAccount(currentUser.getAccount().subtract(transactionDto.getAmount()));
            }

            usersRepo.save(currentUser);
            log.info("Saved transaction successfully");
            kafkaProducer.sendMessage(transactionDto);
        } else {
            throw new NotCompatibleCategory("Type of category " + transactionDto.getCategory()
                    + " is not compatible with transaction type " + transactionDto.getTransactionType());
        }

    }

}
