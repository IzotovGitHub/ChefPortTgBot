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
import ru.izotov.enums.UserStatus;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.SendMessageService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.izotov.enums.Command.*;

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
        Optional<AppUser> userOptional = appUserService.findAppUserByTelegramId(update.getMessage().getFrom().getId());

        if (userOptional.isEmpty()) {
            return String.format(answerConfiguration.getUserNotAuthTemplate(), AUTH.getCommand());
        }

        AppUser user = userOptional.get();
        String result;
        switch (user.getStatus()) {
            case WAITING_FOR_EMAIL -> result = setEmailAndSendVerifyCode(user, update);
            default -> result = String.format(answerConfiguration.getErroneousActionTemplate(), HELP.getCommand());
        }
        return result;
    }

    private String setEmailAndSendVerifyCode(AppUser appUser, Update update) {
        if (nonNull(appUser.getEmail())) {
            return answerConfiguration.getMailAlreadyBeenSend();
        }

        String email = update.getMessage().getText();

        if (!isValidEmail(email)) {
            log.warn(String.format("User with id '%d' entered an invalid email: %s", appUser.getId(), email));
            return String.format(answerConfiguration.getInvalidEmailTemplate(), CANCEL.getCommand());
        }

        if (appUserService.isEmailAlreadyInUse(email)) {
            log.warn(String.format("User with id '%d' entered an email '%s' that is already in use", appUser.getId(), email));
            return String.format(answerConfiguration.getEmailAlreadyInUseTemplate(), CANCEL.getCommand());
        }

        appUser.setEmail(email);
        appUser.setStatus(UserStatus.AWAITING_CONFIRMATION);
        appUserService.update(appUser);


        return answerConfiguration.getDefaultAnswer();
    }

    private boolean isValidEmail(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}
