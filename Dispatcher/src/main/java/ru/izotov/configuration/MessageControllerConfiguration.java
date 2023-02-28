package ru.izotov.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.izotov.controller.MessageController;
import ru.izotov.controller.impl.TextMessageControllerImpl;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("messageControllerBean.properties")
public class MessageControllerConfiguration {

    @Value("${text.controller}")
    private String textControllerKey;

    @Autowired
    private TextMessageControllerImpl textController;


    @Bean
    public Map<String, MessageController> getControllerMap() {
        Map<String, MessageController> result = new HashMap<>();
        result.put(textControllerKey, textController);
        return result;
    }
}
