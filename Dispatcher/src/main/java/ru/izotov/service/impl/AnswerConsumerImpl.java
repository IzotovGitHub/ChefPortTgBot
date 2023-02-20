package ru.izotov.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.izotov.bot.TelegramBot;
import ru.izotov.service.AnswerConsumer;

import static ru.izotov.config.RabbitMqConfig.ANSWER_MESSAGE;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {

    @Autowired
    private TelegramBot bot;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
