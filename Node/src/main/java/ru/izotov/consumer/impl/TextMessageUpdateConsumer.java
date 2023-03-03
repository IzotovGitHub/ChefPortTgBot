package ru.izotov.consumer.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.configuration.AnswerConfiguration;
import ru.izotov.consumer.UpdateConsumer;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.entity.AppUser;
import ru.izotov.enums.Command;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.SendMessageService;

import java.util.Map;

import static java.util.Objects.isNull;
import static ru.izotov.enums.Command.AUTH;
import static ru.izotov.enums.Command.HELP;

@Log4j
@Service
@PropertySource("rabbitmq.properties")
public class TextMessageUpdateConsumer implements UpdateConsumer {

    private final RawDataService rawDataService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqConfig rabbitMqConfig;
    private final AppUserService appUserService;
    private final SendMessageService sendMessageService;
    private final AnswerConfiguration answerConfiguration;
    @Autowired
    @Qualifier("getHandlerMap")
    private Map<Command, CommandHandler> commandHandlerMap;

    public TextMessageUpdateConsumer(RawDataService rawDataService, RabbitTemplate rabbitTemplate, RabbitMqConfig rabbitMqConfig, AppUserService appUserService, SendMessageService sendMessageService, AnswerConfiguration answerConfiguration) {
        this.rawDataService = rawDataService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqConfig = rabbitMqConfig;
        this.appUserService = appUserService;
        this.sendMessageService = sendMessageService;
        this.answerConfiguration = answerConfiguration;
    }

    @Override
    @RabbitListener(queues = "${queue.text.update}")
    public void consume(Update update) {
        rawDataService.saveRawData(update);
        Command command = Command.fromValue(update.getMessage().getText());
        String message;
        if (isNull(command) || !commandHandlerMap.containsKey(command)) {
            message = processTextMessage(update);
        } else {
            message = commandHandlerMap.get(command).handle(update);
        }
        SendMessage messageToSend = sendMessageService.getSendMessage(message, update);
        rabbitTemplate.convertAndSend(rabbitMqConfig.getTextAnswerQueue(), messageToSend);
    }

    private String processTextMessage(Update update) {
        AppUser user = appUserService.findAppUserByTelegramId(update.getMessage().getFrom().getId());
        if (isNull(user)) {
            return String.format(answerConfiguration.getUserNotAuthTemplate(), AUTH.getCommand());
        }

        String result;
        switch (user.getStatus()) {
            case WAITING_FOR_EMAIL -> result = setEmailAndSendVerifyCode(update);
            default -> result = String.format(answerConfiguration.getErroneousActionTemplate(), HELP.getCommand());
        }
        return result;
    }

    private String setEmailAndSendVerifyCode(Update update) {
        return answerConfiguration.getDefaultAnswer();
    }
}
