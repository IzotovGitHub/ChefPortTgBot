package ru.izotov.bot.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.izotov.bot.TelegramBot;
import ru.izotov.controller.UpdateController;
import ru.izotov.service.SendMessageService;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Log4j
@Component
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    private final SendMessageService sendMessageService;
    @Autowired
    private Set<UpdateController> updateControllers;

    public TelegramBotImpl(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        checkArgument(update.hasMessage(), "Received update has not a message");
        updateControllers.stream()
                .filter(controller -> controller.isNeedProcess(update))
                .forEach(controller -> controller.process(update));
    }

    @Override
    public void sendAnswerMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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
