package ru.izotov.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.dao.mapper.UserMapper;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.entity.AppUser;
import ru.izotov.service.ConsumerService;
import ru.izotov.service.ProducerService;

import static java.util.Objects.isNull;
import static ru.izotov.config.RabbitMqConfig.TEXT_UPDATE_MESSAGE;

@Log4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final RawDataService rawDataService;
    private final AppUserService appUserService;
    private final ProducerService producerService;
    private final UserMapper userMapper;

    @Override
    @RabbitListener(queues = TEXT_UPDATE_MESSAGE)
    public void consumeTextMessageUpdates(Update update) {
        rawDataService.saveRawData(update);
        User telegramUser = update.getMessage().getFrom();
        AppUser appUser = appUserService.findAppUserByTelegramId(telegramUser.getId());
        if (isNull(appUser)) {
            appUser = appUserService.create(userMapper.toAppUser(telegramUser));
        }
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello " + appUser.getFirstName());
        producerService.produceAnswer(sendMessage);
    }
}
