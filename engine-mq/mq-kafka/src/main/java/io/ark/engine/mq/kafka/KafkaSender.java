package io.ark.engine.mq.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.engine.mq.core.message.MqMessage;
import io.ark.engine.mq.core.sender.MessageSender;
import io.ark.engine.mq.core.sender.SendCallback;
import io.ark.framework.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@RequiredArgsConstructor
public class KafkaSender implements MessageSender {

    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void send(MqMessage message) {
        kafkaTemplate.send(message.getTopic(),message.getBody());
    }

    @Override
    public void sendAsync(MqMessage message, SendCallback callback) {
        MessageSender.super.sendAsync(message, callback);
    }

    @Override
    public void publish(DomainEvent event) {

        System.out.println("KafkaSender publish");
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(event.getTag(), payload);
    }

    @Override
    public void publishAll(Iterable<? extends DomainEvent> events) {
        MessageSender.super.publishAll(events);
    }
}
