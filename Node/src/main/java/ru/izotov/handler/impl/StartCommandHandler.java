package ru.izotov.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.entity.AppUser;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.enums.Command;

import static ru.izotov.service.enums.Command.START;

@Component
public class StartCommandHandler implements CommandHandler {
    @Override
    public String handle(AppUser user, Update update) {
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
