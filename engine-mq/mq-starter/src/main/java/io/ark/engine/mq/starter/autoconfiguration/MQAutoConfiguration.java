package io.ark.engine.mq.starter.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.engine.mq.core.sender.MessageSender;
import io.ark.engine.mq.kafka.KafkaSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@AutoConfiguration
public class MQAutoConfiguration {

    @Bean
    public MessageSender messageSender(KafkaTemplate  template, ObjectMapper objectMapper) {
        return new KafkaSender(template, objectMapper);
    }
}
