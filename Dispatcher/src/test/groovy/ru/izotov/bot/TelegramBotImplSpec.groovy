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
        bot = new TelegramBotImpl(sendMessageService)
        bot.@botName = "bot_name"
        bot.@botToken = "bot_token"
        bot.@textControllerBeanName = "text_message_key"
    }

    def "process update without it"() {
        when: "method is called"
            bot.onUpdateReceived(null)
        then: "exception is thrown"
            def e = thrown(NullPointerException)
            e.getMessage() == "update is marked non-null but is null"
    }

    def "process update without message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> false
            }
        when: "method is called"
            bot.onUpdateReceived(update)
        then: "exception is thrown"
            def e = thrown(IllegalArgumentException)
            e.getMessage() == "Received update has not a message"
    }

    def "received update has a text message"() {
        given: "update"
            def update = Mock(Update) {
                hasMessage() >> true
            }
        and: "messageControllerMap"
            bot.@messageControllerMap = Map.of("text_message_key", textMessageController)
        when: "method is called"
            bot.onUpdateReceived(update)
        then: "get message from update"
            update.getMessage() >> Mock(Message) {
                hasText() >> true
            }
        and: "process text message"
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
