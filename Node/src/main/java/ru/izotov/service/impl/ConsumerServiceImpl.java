package ru.izotov.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.dao.mapper.UserMapper;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.entity.AppUser;
import ru.izotov.service.ConsumerService;
import ru.izotov.service.ProducerService;
import ru.izotov.service.SendMessageService;

import static java.util.Objects.isNull;

@Log4j
@Service
@AllArgsConstructor
@PropertySource("rabbitmq.properties")
public class ConsumerServiceImpl implements ConsumerService {

    private final RawDataService rawDataService;
    private final AppUserService appUserService;
    private final ProducerService producerService;
    private final UserMapper userMapper;
    private final SendMessageService sendMessageService;

    @Override
    @RabbitListener(queues = "${queue.text.update}")
    public void consumeTextMessageUpdates(Update update) {
        rawDataService.saveRawData(update);
        User telegramUser = update.getMessage().getFrom();
        AppUser appUser = appUserService.findAppUserByTelegramId(telegramUser.getId());
        if (isNull(appUser)) {
            appUser = appUserService.create(userMapper.toAppUser(telegramUser));
        }
        producerService.produceAnswer(sendMessageService.getSendMessage(
                "Hello " + appUser.getFirstName(),
                update
        ));
    }
}
