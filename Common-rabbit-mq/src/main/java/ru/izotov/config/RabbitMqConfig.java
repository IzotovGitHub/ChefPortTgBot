package ru.izotov.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
    public static final String TEXT_UPDATE_MESSAGE = "text_message_update";
    public static final String DOC_UPDATE_MESSAGE = "doc_message_update";
    public static final String PHOTO_UPDATE_MESSAGE = "photo_message_update";
    public static final String ANSWER_MESSAGE = "answer_message";
}
