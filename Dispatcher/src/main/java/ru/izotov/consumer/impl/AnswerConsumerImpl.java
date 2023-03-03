package ru.izotov.consumer.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.izotov.bot.TelegramBot;
import ru.izotov.consumer.AnswerConsumer;

@Service
@PropertySource("rabbitmq.properties")
public class AnswerConsumerImpl implements AnswerConsumer {

    @Autowired
    private TelegramBot bot;

    @Override
    @RabbitListener(queues = "${queue.text.answer}")
    public void consumeTextAnswer(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
