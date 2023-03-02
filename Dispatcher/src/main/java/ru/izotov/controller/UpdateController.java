package ru.izotov.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateController {

    /**
     * The method processes text messages from the received update
     *
     * @param update This object represents an incoming update
     */
    void process(Update update);

    boolean isNeedProcess(Update update);
}
