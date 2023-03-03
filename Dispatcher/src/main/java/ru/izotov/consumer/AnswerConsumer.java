package ru.izotov.consumer;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerConsumer {
    void consumeTextAnswer(SendMessage sendMessage);
}
