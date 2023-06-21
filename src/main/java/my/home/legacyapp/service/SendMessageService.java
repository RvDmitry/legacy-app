package my.home.legacyapp.service;

import lombok.RequiredArgsConstructor;
import my.home.legacyapp.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final Logger logger = LoggerFactory.getLogger(SendMessageService.class);
    private final Producer producer;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Value("${spring.kafka.producer.key}")
    private String key;

    public void sendMessage(MessageDto message) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, MessageDto>> completableFuture = this.producer.sendMessage(topic, key, message);
        SendResult<String, MessageDto> result = completableFuture.get();
        logger.info("""
                        Produced:
                        topic: {}
                        partition: {}
                        offset: {}
                        value size: {}
                        message: {}""",
                topic,
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().serializedValueSize(),
                message
        );
    }
}
