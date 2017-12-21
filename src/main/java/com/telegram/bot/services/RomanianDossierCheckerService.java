package com.telegram.bot.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.telegram.bot.domain.Subject;
import com.telegram.bot.services.parsers.WebPageParser;

@Service
public class RomanianDossierCheckerService {

    private final SubjectHandler subjectHandler;

    private final WebPageParser webPageParser;

    @Autowired
    public RomanianDossierCheckerService(SubjectHandler subjectHandler,
                                         WebPageParser webPageParser) {
        this.subjectHandler = subjectHandler;
        this.webPageParser = webPageParser;
    }

    public List<Subject> findSubjectByDossierId(String dossierId) {
        return subjectHandler.getDossierSubjectsByUri(webPageParser.gePdfLinks())
                .stream()
                .filter(subject -> subject.getId().equalsIgnoreCase(dossierId))
                .collect(Collectors.toList());
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

    public String getResponseMessage(List<Subject> foundSubjects) {
        return foundSubjects.isEmpty() ? "You are not on the list, but no worries soon you will be." : "You are on the list!\nSee the details: " + foundSubjects.get(0) + ".\nCool, huh?";
    }
}
