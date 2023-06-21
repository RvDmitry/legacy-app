package my.home.legacyapp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.home.legacyapp.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    private final Producer producer;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Value("${spring.kafka.producer.key}")
    private String key;

    @Async
    @SneakyThrows
    @Override
    public void sendMessage(MessageDto message) {
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
