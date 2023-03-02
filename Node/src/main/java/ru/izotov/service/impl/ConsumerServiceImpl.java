package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.dao.mapper.UserMapper;
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
    private final AppUserService appUserService;
    private final ProducerService producerService;
    private final UserMapper userMapper;
    private final SendMessageService sendMessageService;
    @Autowired
    @Qualifier("getHandlerMap")
    private Map<Command, CommandHandler> commandHandlerMap;


    public ConsumerServiceImpl(RawDataService rawDataService, AppUserService appUserService, ProducerService producerService, UserMapper userMapper, SendMessageService sendMessageService) {
        this.rawDataService = rawDataService;
        this.appUserService = appUserService;
        this.producerService = producerService;
        this.userMapper = userMapper;
        this.sendMessageService = sendMessageService;
    }

    @Override
    @RabbitListener(queues = "${queue.text.update}")
    public void consumeTextMessageUpdates(Update update) {
        rawDataService.saveRawData(update);
        Command command = Command.fromValue(update.getMessage().getText());
        String message;
        if (isNull(command) || !commandHandlerMap.containsKey(command)) {
            message = "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        } else {
            message = commandHandlerMap.get(command).handle(getAppUser(update), update);
        }
        producerService.produceAnswer(sendMessageService.getSendMessage(message, update));
    }

    private AppUser getAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser appUser = appUserService.findAppUserByTelegramId(telegramUser.getId());
        if (isNull(appUser)) {
            appUser = appUserService.create(userMapper.toAppUser(telegramUser));
        }
        return appUser;
    }
}
