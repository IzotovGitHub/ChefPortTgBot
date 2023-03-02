package ru.izotov.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("rabbitmq.properties")
public class RabbitMqConfig {

    // System
    @Value("${host}")
    private String host;
    @Value("${port}")
    private String port;
    @Value("${username}")
    private String userName;
    @Value("${password}")
    private String password;

    // Queue
    @Value("${queue.text.update}")
    private String textQueue;
    @Value("${queue.answer.message}")
    private String answerQueue;
    @Value("${queue.auth.user}")
    private String authUserQueue;
}
