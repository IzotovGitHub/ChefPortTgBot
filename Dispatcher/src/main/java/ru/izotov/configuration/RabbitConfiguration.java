package ru.izotov.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.izotov.config.RabbitMqConfig;

@Configuration
public class RabbitConfiguration {

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(rabbitMqConfig.getTextQueue());
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(rabbitMqConfig.getAnswerQueue());
    }
}
