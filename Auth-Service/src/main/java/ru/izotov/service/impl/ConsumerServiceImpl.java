package ru.izotov.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.configuration.AnswerConfiguration;
import ru.izotov.dao.mapper.UserMapper;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.entity.AppUser;
import ru.izotov.enums.UserStatus;
import ru.izotov.service.ConsumerService;

import static java.util.Objects.isNull;

@Log4j
@Service
@AllArgsConstructor
@PropertySource("rabbitmq.properties")
public class ConsumerServiceImpl implements ConsumerService {

    private final UserMapper userMapper;
    private final AppUserService appUserService;
    private final AnswerConfiguration answerConfiguration;

    @Override
    @RabbitListener(queues = "${queue.auth.user}")
    public String consumeAuthUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser appUser = appUserService.findAppUserByTelegramId(telegramUser.getId())
                .orElse(appUserService.create(userMapper.toAppUser(telegramUser)));

        if (appUser.getIsActive()) {
            return answerConfiguration.getAlreadyActiveAnswer();
        }

        if (isNull(appUser.getEmail())) {
            appUserService.updateStatus(appUser, UserStatus.WAITING_FOR_EMAIL);
            return answerConfiguration.getRequestEmailAnswer();
        }

        return answerConfiguration.getDefaultAnswer();
    }
}
