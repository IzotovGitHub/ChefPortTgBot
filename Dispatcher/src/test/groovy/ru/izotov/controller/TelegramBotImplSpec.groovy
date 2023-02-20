package ru.izotov.controller

import org.telegram.telegrambots.meta.api.objects.Update
import ru.izotov.bot.impl.TelegramBotImpl
import ru.izotov.controller.impl.TextMessageControllerImpl
import spock.lang.Specification

class TelegramBotImplSpec extends Specification {

    TelegramBotImpl bot

    TextMessageControllerImpl updateController

    def setup() {
        updateController = Mock(TextMessageControllerImpl)
        bot = new TelegramBotImpl(updateController, sendMessageService)
        bot.@botName = "bot_name"
        bot.@botToken = "bot_token"
    }

    def "bot registration during initialization"() {
        when: "method is called"
            bot.init()
        then: "the bot is registered"
            1 * updateController.registerBot(bot)
    }

    def "process update test"() {
        given: "update object"
            def update = Mock(Update)
        when: "method is called"
            bot.onUpdateReceived(update)
        then: "update is processed"
            1 * updateController.processMessage(update)
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
