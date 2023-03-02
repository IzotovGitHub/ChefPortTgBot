package ru.izotov.bot


import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.bot.impl.TelegramBotImpl
import ru.izotov.controller.impl.TextMessageUpdateControllerImpl
import ru.izotov.service.SendMessageService
import spock.lang.Specification

class TelegramBotImplSpec extends Specification {

    TelegramBotImpl bot

    SendMessageService sendMessageService
    TextMessageUpdateControllerImpl textMessageController

    def setup() {
        sendMessageService = Mock(SendMessageService)
        textMessageController = Mock(TextMessageUpdateControllerImpl)
        bot = new TelegramBotImpl(sendMessageService)
        bot.@botName = "bot_name"
        bot.@botToken = "bot_token"
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

    def "the bot contains the necessary controller to process the update"() {
        given: "messageControllerMap"
            bot.@updateControllers = [textMessageController].toSet()
        when: "method is called"
            bot.onUpdateReceived(Mock(Update) { hasMessage() >> true })
        then: "find the right controller"
            1 * textMessageController.isNeedProcess({ Update update -> update.hasMessage() }) >> true
        and: "process text message"
            1 * textMessageController.process({ Update update -> update.hasMessage() })
    }

    def "the bot does not contains the necessary controller to process the update"() {
        given: "messageControllerMap"
            bot.@updateControllers = [textMessageController].toSet()
        when: "method is called"
            bot.onUpdateReceived(Mock(Update) { hasMessage() >> true })
        then: "find the right controller"
            1 * textMessageController.isNeedProcess({ Update update -> update.hasMessage() }) >> false
        and: "no more interactions"
            0 * textMessageController._
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
