package ru.izotov.consumer.impl;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.configuration.AnswerConfiguration;
import ru.izotov.consumer.UpdateConsumer;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.dao.service.UserCodeService;
import ru.izotov.entity.AppUser;
import ru.izotov.entity.UserCode;
import ru.izotov.enums.Command;
import ru.izotov.enums.UserCodeStatus;
import ru.izotov.enums.UserStatus;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.CryptoService;
import ru.izotov.service.RestService;
import ru.izotov.service.SendMessageService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.izotov.enums.Command.*;
import static ru.izotov.enums.UserStatus.ACTIVE;

@Log4j
@Service
@PropertySource("rabbitmq.properties")
public class TextMessageUpdateConsumer implements UpdateConsumer {

    @Value("${service.mail.uri}")
    private String mailServiceUrl;

    @Autowired
    private RawDataService rawDataService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private AnswerConfiguration answerConfiguration;
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private UserCodeService userCodeService;
    @Autowired
    private RestService restService;
    @Autowired
    @Qualifier("getHandlerMap")
    private Map<Command, CommandHandler> commandHandlerMap;


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
            case AWAITING_CONFIRMATION -> result = verifyCode(user, update);
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

        String key = getRandomKey();
        UserCode userCode = UserCode.builder()
                .code(key)
                .appUser(appUser)
                .status(UserCodeStatus.ACTIVE)
                .build();
        userCode = userCodeService.create(userCode);

        ResponseEntity<String> response = restService.sendRequestToMailService(mailServiceUrl, buildEmailContent(email, userCode.getCode()));

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Error when trying to send email");
            return answerConfiguration.getInternalError();
        }

        appUser.setEmail(email);
        appUser.setStatus(UserStatus.AWAITING_CONFIRMATION);
        appUserService.update(appUser);

        return "На вашу электронную почту отправлено письмо с кодом подтверждения. Пожалуйста, пришлите код в этот чат для активации аккаунта:";
    }

    private String verifyCode(AppUser user, Update update) {
        UserCode userCode = userCodeService.findActiveUserCodeByUserId(user.getId());
        if (isNull(userCode)) {
            return "Во время подтверждения кода регистрации произошла ошибка! =(";
        }
        appUserService.updateStatus(user, ACTIVE);
        userCode.setStatus(UserCodeStatus.DELETED);
        userCodeService.delete(userCode);
        return "Регистрация прошла успешно!";
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

    private JSONObject buildEmailContent(String emailTo, String confirmationKey) {
        return new JSONObject(Map.of(
                "emailTo", emailTo,
                "subject", "Активация учетной записи",
                "body", String.format(
                        """
                                Привет! 
                                                        
                                Для активации аккаунта осталось только ввести код: %s
                                                        
                                Спасибо, что вы с нами! 
                                """, confirmationKey
                )
        ));
    }

    private String getRandomKey() {
        int key = new Random().nextInt(9999);

        StringBuilder result = new StringBuilder(String.valueOf(key));
        while (result.length() < 4) {
            result.insert(0, "0");
        }
        return result.toString();
    }
}
