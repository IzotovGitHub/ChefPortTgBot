package ru.izotov.handler.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.configuration.AnswerConfiguration;
import ru.izotov.enums.Command;
import ru.izotov.handler.CommandHandler;

@Log4j
@Component
@AllArgsConstructor
public class CancelCommandHandler implements CommandHandler {

    private final AnswerConfiguration answerConfiguration;

    @Override
    public String handle(Update update) {
        return answerConfiguration.getDefaultAnswer();
    }

    @Override
    public Command getCommand() {
        return Command.CANCEL;
    }
}
