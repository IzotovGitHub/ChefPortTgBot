package ru.izotov.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TextMessageController {

    void process(Update update);
}
