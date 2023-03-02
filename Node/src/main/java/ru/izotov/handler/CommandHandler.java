package ru.izotov.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.entity.AppUser;
import ru.izotov.service.enums.Command;

public interface CommandHandler {

    String handle(AppUser user, Update update);

    Command getCommand();
}
