package ru.izotov.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.service.SendMessageService;

@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Override
    public SendMessage getSendMessage(String message, Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(message)
                .build();
    }
}
