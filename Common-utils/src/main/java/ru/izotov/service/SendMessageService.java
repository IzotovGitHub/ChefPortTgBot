package ru.izotov.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface SendMessageService {

    SendMessage getSendMessage(String message, Update update);
}
