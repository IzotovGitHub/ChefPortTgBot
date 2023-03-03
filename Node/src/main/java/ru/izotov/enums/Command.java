package ru.izotov.enums;

import java.util.Arrays;

public enum Command {
    START("/start", "стартовая команда, для начала работы с ботом."),
    AUTH("/auth", "регистрация пользователя."),
    CANCEL("/cancel", "отмена выполнения текущей команды."),
    HELP("/help", "просмотр списка доступных команд.");

    private final String command;
    private final String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static Command fromValue(String value) {
        return Arrays.stream(Command.values())
                .filter(c -> c.command.equals(value))
                .findFirst()
                .orElse(null);
    }
}
