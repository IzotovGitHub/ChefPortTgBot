package ru.izotov.bot.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.izotov.bot.TelegramBot;
import ru.izotov.controller.MessageController;
import ru.izotov.service.SendMessageService;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Log4j
@Component
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

    private static final String UNSUPPORTED_MESSAGE = "Извините, но я еще не умею обрабатывать такие сообщения! =(";
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Value("${text.controller}")
    private String textControllerBeanName;

    private final SendMessageService sendMessageService;
    @Autowired
    @Qualifier("getControllerMap")
    private Map<String, MessageController> messageControllerMap;

    public TelegramBotImpl(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        checkArgument(update.hasMessage(), "Received update has not a message");
        Message message = update.getMessage();
        if (message.hasText()) {
            messageControllerMap.get(textControllerBeanName).process(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    @Override
    public void sendAnswerMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        sendAnswerMessage(sendMessageService.getSendMessage(UNSUPPORTED_MESSAGE, update));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
