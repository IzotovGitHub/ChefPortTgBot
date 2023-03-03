package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.entity.AppUser;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.ConsumerService;
import ru.izotov.service.ProducerService;
import ru.izotov.service.SendMessageService;
import ru.izotov.service.enums.Command;

import java.util.Map;

import static java.util.Objects.isNull;

@Log4j
@Service
@PropertySource("rabbitmq.properties")
public class ConsumerServiceImpl implements ConsumerService {

    private final RawDataService rawDataService;
    private final ProducerService producerService;
    private final AppUserService appUserService;
    private final SendMessageService sendMessageService;
    @Autowired
    @Qualifier("getHandlerMap")
    private Map<Command, CommandHandler> commandHandlerMap;

    public ConsumerServiceImpl(RawDataService rawDataService, ProducerService producerService, AppUserService appUserService, SendMessageService sendMessageService) {
        this.rawDataService = rawDataService;
        this.producerService = producerService;
        this.appUserService = appUserService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    @RabbitListener(queues = "${queue.text.update}")
    public void consumeTextMessageUpdates(Update update) {
        rawDataService.saveRawData(update);
        Command command = Command.fromValue(update.getMessage().getText());
        String message;
        if (isNull(command) || !commandHandlerMap.containsKey(command)) {
            message = processTextMessage(update);
        } else {
            message = commandHandlerMap.get(command).handle(update);
        }
        producerService.produceAnswer(sendMessageService.getSendMessage(message, update));
    }

    private String processTextMessage(Update update) {
        AppUser user = appUserService.findAppUserByTelegramId(update.getMessage().getFrom().getId());
        if (isNull(user)) {
            return """
                    Вы не зарегистрированы в системе! 
                    Для регистрации введите команду: /auth
                    """;
        }

        String result;
        switch (user.getStatus()) {
            case WAITING_FOR_EMAIL -> result = setEmailAndSendVerifyCode(update);
            default -> result = "Извините, я Вас не понял! Для просмотра доступных команд, пожалуйста, введите: /help";
        }
        return result;
    }

    private String setEmailAndSendVerifyCode(Update update) {
        return "Функционал в разработке =)";
    }
}
