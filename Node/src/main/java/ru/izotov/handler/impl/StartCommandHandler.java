package ru.izotov.handler.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.configuration.AnswerConfiguration;
import ru.izotov.enums.Command;
import ru.izotov.handler.CommandHandler;

import static ru.izotov.enums.Command.START;

@Component
@AllArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private final AnswerConfiguration answerConfiguration;

    @Override
    public String handle(Update update) {
        User user = update.getMessage().getFrom();
        String template = answerConfiguration.getStartTemplate();
        return String.format(template, user.getFirstName(), Command.AUTH.getCommand(), Command.HELP.getCommand());
    }

    @Override
    public Command getCommand() {
        return START;
    }
}
