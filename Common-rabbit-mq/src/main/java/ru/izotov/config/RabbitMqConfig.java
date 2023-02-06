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
    @Value("${queue.doc.update}")
    private String docUpdateQueue;
    @Value("${queue.photo.update}")
    private String photoUpdateQueue;
    @Value("${queue.text.update}")
    private String textUpdateQueue;
    @Value("${queue.answer.message}")
    private String answerMessageQueue;
}
