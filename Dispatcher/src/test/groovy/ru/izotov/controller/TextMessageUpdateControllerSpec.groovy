package ru.izotov.controller

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.config.RabbitMqConfig
import ru.izotov.controller.impl.TextMessageUpdateControllerImpl
import ru.izotov.service.UpdateProducer
import spock.lang.Specification

class TextMessageUpdateControllerSpec extends Specification {

    TextMessageUpdateControllerImpl controller

    UpdateProducer updateProducer
    RabbitMqConfig rabbitMqConfig

    def setup() {
        updateProducer = Mock(UpdateProducer)
        rabbitMqConfig = Mock(RabbitMqConfig)

        controller = new TextMessageUpdateControllerImpl(updateProducer, rabbitMqConfig)
    }

    def "message processing without update"() {
        when: "method is called"
            controller.process(null)
        then: "exception is thrown"
            def e = thrown(NullPointerException)
            e.getMessage() == "update is marked non-null but is null"
    }

    def "received update has no message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> false
            }
        when: "method is called"
            controller.process(update)
        then: "exception id thrown"
            def e = thrown(IllegalArgumentException)
            e.getMessage() == "Received update has not a message"
    }

    def "received update has a text message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> true
            }
        when: "method is called"
            controller.process(update)
        then: "get message from update"
            update.getMessage() >> Mock(Message) {
                hasText() >> true
            }
        and: "get queue key"
            1 * rabbitMqConfig.getTextQueue() >> "text_message_update"
        and: "produce text message"
            1 * updateProducer.produce("text_message_update", update)
    }
}
