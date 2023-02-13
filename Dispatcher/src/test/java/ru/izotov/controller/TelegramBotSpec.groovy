package ru.izotov.controller

import org.telegram.telegrambots.meta.api.objects.Update
import spock.lang.Specification

class TelegramBotSpec extends Specification {

    TelegramBot bot

    UpdateController updateController

    def setup() {
        updateController = Mock(UpdateController)
        bot = new TelegramBot(updateController)
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
            1 * updateController.processUpdate(update)
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
