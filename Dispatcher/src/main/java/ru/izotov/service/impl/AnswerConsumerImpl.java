package ru.izotov.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.izotov.bot.TelegramBot;
import ru.izotov.service.AnswerConsumer;

@Service
@PropertySource("rabbitmq.properties")
public class AnswerConsumerImpl implements AnswerConsumer {

    @Autowired
    private TelegramBot bot;

    @Override
    @RabbitListener(queues = "${queue.answer.message}")
    public void consume(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
