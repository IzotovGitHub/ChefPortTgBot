package ru.izotov.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageController {

    void process(Update update);
}
