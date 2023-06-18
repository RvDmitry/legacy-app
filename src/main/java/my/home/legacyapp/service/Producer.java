package my.home.legacyapp.service;

import lombok.RequiredArgsConstructor;
import my.home.legacyapp.dto.MessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    public CompletableFuture<SendResult<String, MessageDto>> sendMessage(String topic, String key, MessageDto message) {

        return this.kafkaTemplate.send(topic, key, message);
    }
}
