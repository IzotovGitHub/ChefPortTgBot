package ru.izotov.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("bot.answer.properties")
public class AnswerConfiguration {

    @Value("${developing}")
    private String defaultAnswer;
    @Value("${internal.error}")
    private String internalError;
    @Value("${node.start}")
    private String startTemplate;
    @Value("${node.text.user.not.auth}")
    private String userNotAuthTemplate;
    @Value("${node.erroneous.action}")
    private String erroneousActionTemplate;
    @Value("${node.mail.already.been.send}")
    private String mailAlreadyBeenSend;
    @Value("${node.invalid.email}")
    private String invalidEmailTemplate;
    @Value("${node.email.already.in.use}")
    private String emailAlreadyInUseTemplate;

}
