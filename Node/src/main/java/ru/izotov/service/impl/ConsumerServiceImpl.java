package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.service.ConsumerService;
import ru.izotov.service.ProducerService;

import static ru.izotov.config.RabbitMqConfig.*;

@Log4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ProducerService producerService;

    @Override
    @RabbitListener(queues = TEXT_UPDATE_MESSAGE)
    public void consumeTextMessageUpdates(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello form node");
        producerService.produceAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_UPDATE_MESSAGE)
    public void consumeDocMessageUpdates(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello form node");
        producerService.produceAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = PHOTO_UPDATE_MESSAGE)
    public void consumePhotoMessageUpdates(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello form node");
        producerService.produceAnswer(sendMessage);
    }
}
