package com.telegram.bot.bots;

import static com.telegram.bot.utils.MessageUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RomanianDossierCheckerBot.class);

    private final String pdfTelegramBotName;

    private final String pdfTelegramBotToken;

    private final RomanianDossierCheckerService romanianDossierCheckerService;

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
                sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), WELCOMING_MESSAGE));
                break;
            default:
                sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), generateLookupMessage(update.getMessage().getText())));
                findSubjectByDossierIdAndSendMessage(update);
                break;
        }
    }

    private Subject findSubjectByDossierId(String dossierId) {
        return romanianDossierCheckerService.findSubjectByDossierId(dossierId);
    }

    private void findSubjectByDossierIdAndSendMessage(Update update) {
        Subject subject = findSubjectByDossierId(update.getMessage().getText());
        LOGGER.info("Found subject by id {}: {}", update.getMessage().getText(), subject);
        sendMessageBack(romanianDossierCheckerService.prepareMessage(update.getMessage().getChatId(), romanianDossierCheckerService.getResponseMessage(subject)));
    }

    private void sendMessageBack(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.debug("Error during sending message: {}", e);
        }
    }
}