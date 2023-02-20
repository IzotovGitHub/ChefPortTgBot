package ru.izotov.controller.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.controller.TextMessageController;
import ru.izotov.service.UpdateProducer;

import static java.util.Objects.isNull;
import static ru.izotov.config.RabbitMqConfig.TEXT_UPDATE_MESSAGE;

@Log4j
@Component
public class TextMessageControllerImpl implements TextMessageController {

    private final UpdateProducer updateProducer;

    public TextMessageControllerImpl(UpdateProducer updateProducer) {
        this.updateProducer = updateProducer;
    }


    /**
     * The method processes text messages from the received update
     *
     * @param update This object represents an incoming update
     */
    @Override
    public void process(Update update) {
        if (isNull(update)) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            updateProducer.produce(TEXT_UPDATE_MESSAGE, update);
        } else {
            log.warn("Unsupported message is received: " + update);
        }
    }
}
