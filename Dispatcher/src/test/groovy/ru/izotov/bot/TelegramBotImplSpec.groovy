package ru.izotov.bot


import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.bot.impl.TelegramBotImpl
import ru.izotov.controller.impl.TextMessageControllerImpl
import ru.izotov.service.SendMessageService
import spock.lang.Specification

class TelegramBotImplSpec extends Specification {

    TelegramBotImpl bot

    SendMessageService sendMessageService
    TextMessageControllerImpl textMessageController

    def setup() {
        sendMessageService = Mock(SendMessageService)
        textMessageController = Mock(TextMessageControllerImpl)
        bot = new TelegramBotImpl(textMessageController, sendMessageService)
        bot.@botName = "bot_name"
        bot.@botToken = "bot_token"
    }

    def "process update with unsupported message type"() {
        given: "update object"
            def update = Mock(Update) { it ->
                it.hasMessage() >> false
            }
        when: "method is called"
            bot.onUpdateReceived(update)
        then: "get message from update"
            def e = thrown(IllegalArgumentException)
            e.getMessage() == "Received update has not a message"
    }

    def "process update test"() {
        given: "update object"
            def update = Mock(Update) { it ->
                it.hasMessage() >> true
            }
        when: "method is called"
            bot.onUpdateReceived(update)
        then: "get message from update"
            1 * update.getMessage() >> Mock(Message) { it ->
                it.hasText() >> true
            }
        and: "update is processed"
            1 * textMessageController.process(update)
    }

    def "get bot name"() {
        when: "method is called"
            def name = bot.getBotUsername()
        then: "should return the expected result"
            name == "bot_name"
    }

    def "get bot token"() {
        when: "method is called"
            def name = bot.getBotToken()
        then: "should return the expected result"
            name == "bot_token"
    }
}
