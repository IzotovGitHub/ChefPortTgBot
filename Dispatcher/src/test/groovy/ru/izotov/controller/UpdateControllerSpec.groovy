package ru.izotov.controller

import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.service.UpdateProducer
import spock.lang.Specification

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

    def "received update has message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> true
            }
        and: "bot is initialized"
            updateController.registerBot(telegramBot)
        when: "method is called"
            updateController.processMessage(update)
        then: "nothing happened"
            0 * updateProducer._
            noExceptionThrown()
    }
}
