package com.telegram.bot.bots;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.telegram.bot.domain.Subject;
import com.telegram.bot.services.RomanianDossierCheckerService;

/**
 * Service which contains pdf bot's logic
 */
@Service
public class RomanianDossierCheckerBot extends TelegramLongPollingBot {

    private final String pdfTelegramBotName;

    private final String pdfTelegramBotToken;

    private final RomanianDossierCheckerService romanianDossierCheckerService;

    private static final String START_COMMAND = "/start";

    @Autowired
    public RomanianDossierCheckerBot(@Value("${settings.bot.name}") String pdfTelegramBotName,
                                     @Value("${settings.bot.token}") String pdfTelegramBotToken,
                                     RomanianDossierCheckerService romanianDossierCheckerService) {
        this.pdfTelegramBotName = pdfTelegramBotName;
        this.pdfTelegramBotToken = pdfTelegramBotToken;
        this.romanianDossierCheckerService = romanianDossierCheckerService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (romanianDossierCheckerService.hasChatIdAndMessage(update)) {
            executeOnUpdate(update);
        }
    }

    @Override
    public String getBotUsername() {
        return pdfTelegramBotName;
    }

    @Override
    public String getBotToken() {
        return pdfTelegramBotToken;
    }

    private void executeOnUpdate(Update update) {
        handleRequests(update);
    }

    private void handleRequests(Update update) {
        switch(update.getMessage().getText()) {
            case START_COMMAND:
                sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), "Willkommen doamna si domnule, \n\nI am here to check state of your dossier." +
                        "\n\nJust type your ID (dossier number: number/year, Ex: 1763/2013) below:"));
                break;
            default:
                sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), "I am looking for a dossier by ID: " + update.getMessage().getText()
                        + "\nMostly it takes up to 7 minutes. Make yourself a cup of coffee :)."));
                findSubjectByDossierIdAndSendMessage(update);
                break;
        }
    }

    private void findSubjectByDossierIdAndSendMessage(Update update) {
        List<Subject> subjects = romanianDossierCheckerService.findSubjectByDossierId(update.getMessage().getText());
        System.out.println("Subjects are: " + subjects);
        sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), romanianDossierCheckerService.getResponseMessage(subjects)));
    }

    private void sendMessageBack(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error during sending message: " + e);
        }
    }
}