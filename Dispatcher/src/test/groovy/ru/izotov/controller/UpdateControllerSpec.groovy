package ru.izotov.controller

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.service.UpdateProducer
import spock.lang.Specification

import static ru.izotov.controller.UpdateController.UNSUPPORTED_MESSAGE

class UpdateControllerSpec extends Specification {

    UpdateController updateController

    TelegramBot telegramBot
    UpdateProducer updateProducer

    def setup() {
        telegramBot = Mock(TelegramBot)
        updateProducer = Mock(UpdateProducer)

        updateController = new UpdateController(updateProducer)
    }

    def "register bot"() {
        when: "method is called"
            updateController.registerBot(telegramBot)
        then: "the telegram bot is initialized"
            updateController.@telegramBot == telegramBot
    }

    def "message processing without update"() {
        when: "method is called"
            updateController.processMessage(null)
        then: "nothing happened"
            0 * updateProducer._
            noExceptionThrown()
    }

    def "message processing when bot is not initialized"() {
        when: "method is called"
            updateController.processMessage(Mock(Update))
        then: "nothing happened"
            0 * updateProducer._
            noExceptionThrown()
    }

    def "received update has no message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> false
            }
        and: "bot is initialized"
            updateController.registerBot(telegramBot)
        when: "method is called"
            updateController.processMessage(update)
        then: "nothing happened"
            0 * updateProducer._
            noExceptionThrown()
    }

    def "received update has a text message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> true
            }
        and: "bot is initialized"
            updateController.registerBot(telegramBot)
        when: "method is called"
            updateController.processMessage(update)
        then: "get message from update"
            update.getMessage() >> Mock(Message) {
                hasText() >> true
            }
        and: "produce text message"
            1 * updateProducer.produce("text_message_update", update)
    }

    def "received update has a text message with unsupported type"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> true
            }
        and: "bot is initialized"
            updateController.registerBot(telegramBot)
        when: "method is called"
            updateController.processMessage(update)
        then: "get message from update"
            update.getMessage() >> Mock(Message) {
                hasText() >> false
                getChatId() >> 1L
            }
        and: "send answer"
            1 * telegramBot.execute({ SendMessage message ->
                message.getChatId() == "1"
                message.getText() == UNSUPPORTED_MESSAGE
            })
    }

    def "send answer message test"() {
        given: "send message"
            def message = Mock(SendMessage)
        and: "bot is initialized"
            updateController.registerBot(telegramBot)
        when: "method is called"
            updateController.sendAnswerMessage(message)
        then: "send message to bot"
            1 * telegramBot.execute(message)
    }
}
