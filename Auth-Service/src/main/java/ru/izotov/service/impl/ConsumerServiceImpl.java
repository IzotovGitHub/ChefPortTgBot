package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.service.ConsumerService;

@Log4j
@Service
@PropertySource("rabbitmq.properties")
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    @RabbitListener(queues = "${queue.auth.user}")
    public String consumeAuthUser(Update update) {
        return "Функционал в разработке =)";
    }
}
