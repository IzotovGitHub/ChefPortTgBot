package ru.izotov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.service.ProducerService;

@Service
@AllArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqConfig rabbitMqConfig;

    @Override
    public void produceAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(rabbitMqConfig.getTextAnswerQueue(), sendMessage);
    }
}
