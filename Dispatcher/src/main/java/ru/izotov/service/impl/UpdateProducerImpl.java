package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.config.RabbitMqConfig;
import ru.izotov.service.UpdateProducer;

@Log4j
@Service
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitMqConfig rabbitMqConfig;

    public UpdateProducerImpl(RabbitMqConfig rabbitMqConfig) {
        this.rabbitMqConfig = rabbitMqConfig;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
    }
}
