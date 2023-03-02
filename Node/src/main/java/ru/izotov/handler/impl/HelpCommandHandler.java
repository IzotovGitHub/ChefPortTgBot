package ru.izotov.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.entity.AppUser;
import ru.izotov.handler.CommandHandler;
import ru.izotov.service.enums.Command;

import java.util.Arrays;

import static ru.izotov.service.enums.Command.HELP;

@Component
public class HelpCommandHandler implements CommandHandler {
    @Override
    public String handle(AppUser user, Update update) {
        StringBuilder builder = new StringBuilder("Список доступных команд:\n\n");
        Arrays.stream(Command.values())
                .forEach(command -> builder.append(command.getCommand())
                        .append(" - ")
                        .append(command.getDescription())
                        .append("\n"));
        return builder.toString();
    }

    @Override
    public Command getCommand() {
        return HELP;
    }
}
