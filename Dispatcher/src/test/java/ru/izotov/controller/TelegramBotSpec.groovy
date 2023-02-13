package ru.izotov.controller

import spock.lang.Specification

class TelegramBotSpec extends Specification {

    TelegramBot bot

    UpdateController updateController

    def setup() {
        updateController = Mock(UpdateController)
        bot = new TelegramBot(updateController)
    }

    def "bot registration during initialization"() {
        when: "method is called"
        bot.init()
        then: "the bot is registered"
        1 * updateController.registerBot(bot)
    }
}
