package ru.izotov.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.service.UpdateProducer;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Log4j
@Component
public class UpdateController {

    private final RabbitMqConfig rabbitMqConfig;
    private final UpdateProducer updateProducer;
    private TelegramBot telegramBot;

    public UpdateController(RabbitMqConfig rabbitMqConfig, UpdateProducer updateProducer) {
        this.rabbitMqConfig = rabbitMqConfig;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (isNull(update)) {
            log.error("Received update is null");
            return;
        } else if (isNull(telegramBot)) {
            log.error("Bot is not initialized");
            return;
        }

        if (nonNull(update.getMessage())) {
            distributeMessagesByType(update);
        } else {
            log.warn("Unsupported message is received: " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        Message message = update.getMessage();
        if (nonNull(message.getText())) {
            processTextMessage(update);
        } else if (nonNull(message.getDocument())) {
            processDocMessage(update);
        } else if (nonNull(message.getPhoto())) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(rabbitMqConfig.getTextUpdateQueue(), update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(rabbitMqConfig.getDocUpdateQueue(), update);
        sendMessage("Файл получен! Обрабатывается...", update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(rabbitMqConfig.getPhotoUpdateQueue(), update);
        sendMessage("Фото получено! Обрабатывается...", update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        sendMessage("Неподдерживаемый тип сообжения!", update);
    }

    private void sendMessage(String message, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(message);
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}