package com.telegram.bot.services;

import java.net.URI;
import java.util.List;

import com.telegram.bot.domain.Subject;

public interface SubjectHandler {
    List<Subject> getDossierSubjectsByUri(URI pdfUri);
}
