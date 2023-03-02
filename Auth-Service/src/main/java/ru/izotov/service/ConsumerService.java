package ru.izotov.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    String consumeAuthUser(Update update);
}
