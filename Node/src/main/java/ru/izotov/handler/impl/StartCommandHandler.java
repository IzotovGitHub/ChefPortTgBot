package ru.izotov.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.enums.Command;
import ru.izotov.handler.CommandHandler;

import static ru.izotov.enums.Command.START;

@Component
public class StartCommandHandler implements CommandHandler {
    @Override
    public String handle(Update update) {
        User user = update.getMessage().getFrom();
        String template = """
                Приветствую, %s!
                 
                 - Для полноценного взаимодействия с ботом пройдте регистрацию %s
                 - Чтобы посмотреть список доступных команд введите %s
                """;
        return String.format(template, user.getFirstName(), Command.AUTH.getCommand(), Command.HELP.getCommand());
    }

    @Override
    public Command getCommand() {
        return START;
    }
}
