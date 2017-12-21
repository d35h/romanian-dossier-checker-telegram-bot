package com.telegram.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;

import com.telegram.bot.bots.RomanianDossierCheckerBot;

/**
 * This components deals with registration of a bot to telegram platform
 */
@Component
public class TelegramBotRegister implements CommandLineRunner {

    private RomanianDossierCheckerBot xmlTelegramBot;

    @Autowired
    public TelegramBotRegister(RomanianDossierCheckerBot xmlTelegramBot) {
        this.xmlTelegramBot = xmlTelegramBot;
    }

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(xmlTelegramBot);
    }
}

