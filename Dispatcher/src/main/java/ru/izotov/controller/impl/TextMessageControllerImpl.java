package ru.izotov.controller.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.controller.MessageController;
import ru.izotov.service.UpdateProducer;

import static com.google.common.base.Preconditions.checkArgument;

@Log4j
@AllArgsConstructor
@Component
@Qualifier("${text.controller}")
@PropertySource("messageControllerBean.properties")
public class TextMessageControllerImpl implements MessageController {
    private final UpdateProducer updateProducer;
    private final RabbitMqConfig rabbitMqConfig;


    /**
     * The method processes text messages from the received update
     *
     * @param update This object represents an incoming update
     */
    @Override
    public void process(@NonNull Update update) {
        checkArgument(update.hasMessage(), "Received update has not a message");
        Message message = update.getMessage();

        if (message.hasText()) {
            updateProducer.produce(rabbitMqConfig.getTextQueue(), update);
        } else {
            log.warn("Unsupported message is received: " + update);
        }
    }
}
