package ru.izotov.consumer;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateConsumer {
    void consume(Update update);
}
