package ru.izotov.controller.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.controller.UpdateController;

import static com.google.common.base.Preconditions.checkArgument;

@Log4j
@Component
@AllArgsConstructor
public class TextMessageUpdateControllerImpl implements UpdateController {
    private final RabbitMqConfig rabbitMqConfig;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void process(@NonNull Update update) {
        checkArgument(update.hasMessage(), "Received update has not a message");
        Message message = update.getMessage();

        if (message.hasText()) {
            rabbitTemplate.convertAndSend(rabbitMqConfig.getTextQueue(), update);
        } else {
            log.warn("Unsupported message is received: " + update);
        }
    }

    @Override
    public boolean isNeedProcess(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
