package com.telegram.bot.services;

import static com.telegram.bot.utils.MessageUtils.*;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.telegram.bot.domain.Subject;
import com.telegram.bot.services.parsers.WebPageParser;

@Service
public class RomanianDossierCheckerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RomanianDossierCheckerService.class);

    private final SubjectHandler subjectHandler;

    private final WebPageParser webPageParser;

    @Autowired
    public RomanianDossierCheckerService(SubjectHandler subjectHandler,
                                         WebPageParser webPageParser) {
        this.subjectHandler = subjectHandler;
        this.webPageParser = webPageParser;
    }

    public Subject findSubjectByDossierId(String dossierId) {
        LOGGER.info("Looking for a dossier by the following id: {}", dossierId);
        return webPageParser.gePdfLinks()
                .stream()
                .flatMap(link -> subjectHandler.getDossierSubjectsByUri(URI.create(link))
                        .stream()
                        .filter(subj -> subj.getId().equalsIgnoreCase(dossierId))
                )
                .findAny()
                .orElse(null);
    }

    public SendMessage prepareMessage(long chatId, String messageToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageToSend);

        return sendMessage;
    }

    public boolean hasChatIdAndMessage(Update update) {
        return update.getMessage().getChatId() != null && update.getMessage().hasText();
    }

    public String getResponseMessage(Subject foundSubjects) {
        return foundSubjects == null ? NOT_ON_THE_LIST_MESSAGE : generateSuccessfulLookupMessage(foundSubjects);
    }
}
