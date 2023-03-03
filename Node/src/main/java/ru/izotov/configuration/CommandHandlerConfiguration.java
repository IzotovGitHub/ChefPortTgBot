package ru.izotov.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.izotov.enums.Command;
import ru.izotov.handler.CommandHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class CommandHandlerConfiguration {

    @Autowired
    private Set<CommandHandler> handlers;

    @Bean
    public Map<Command, CommandHandler> getHandlerMap() {
        return handlers.stream().collect(Collectors.toMap(CommandHandler::getCommand, Function.identity()));
    }
}
