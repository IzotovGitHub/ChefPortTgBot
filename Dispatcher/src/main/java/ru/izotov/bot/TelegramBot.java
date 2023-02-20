package ru.izotov.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramBot {

    void sendAnswerMessage(SendMessage message);
}
