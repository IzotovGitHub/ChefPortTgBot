package ru.izotov.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.izotov.service.UpdateProducer;

import static java.util.Objects.isNull;
import static ru.izotov.config.RabbitMqConfig.TEXT_UPDATE_MESSAGE;

@Log4j
@Component
public class UpdateController {

    private static final String UNSUPPORTED_MESSAGE = "Извините, но я еще не умею обрабатывать такие сообщения! =(";

    private final UpdateProducer updateProducer;
    private TelegramBot telegramBot;

    public UpdateController(UpdateProducer updateProducer) {
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * The method processes messages from the received update
     *
     * @param update This object represents an incoming update
     */
    public void processMessage(Update update) {
        if (isNull(update)) {
            log.error("Received update is null");
            return;
        } else if (isNull(telegramBot)) {
            // TODO дмуаю, что лучше выбрасывать исключнение
            log.error("Bot is not initialized");
            return;
        }

        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else {
            log.warn("Unsupported message is received: " + update);
        }
    }
}
