package ru.izotov.handler.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.enums.Command;

import static ru.izotov.service.enums.Command.AUTH;

@Log4j
@Component
@AllArgsConstructor
public class AuthCommandHandler implements CommandHandler {

    private final RabbitMqConfig rabbitMqConfig;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public String handle(Update update) {
        try {
            return (String) rabbitTemplate.convertSendAndReceive(rabbitMqConfig.getAuthUserQueue(), update);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Command getCommand() {
        return AUTH;
    }
}
