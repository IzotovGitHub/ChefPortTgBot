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

}
