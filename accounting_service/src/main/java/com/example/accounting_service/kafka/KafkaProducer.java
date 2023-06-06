package com.example.accounting_service.kafka;

import com.example.accounting_service.dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    @Value("${spring.kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, TransactionDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(TransactionDto dto) {
        Message<TransactionDto> transactionDtoMessage = MessageBuilder.withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();
        kafkaTemplate.send(transactionDtoMessage);
    }
}
