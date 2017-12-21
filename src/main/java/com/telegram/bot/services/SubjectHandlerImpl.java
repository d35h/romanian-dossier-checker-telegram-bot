package com.telegram.bot.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telegram.bot.domain.Subject;
import com.telegram.bot.services.parsers.UriParser;
import com.telegram.bot.utils.SubjectUtils;

@Service
public class SubjectHandlerImpl implements SubjectHandler {

    private final static String SPLITTER_SYMBOL = "\\.|\\;";

    private final UriParser uriParser;

    @Autowired
    public SubjectHandlerImpl(UriParser uriParser) {
        this.uriParser = uriParser;
    }

    @Override
    public List<Subject> getDossierSubjectsByUri(List<String> pdfUris) {
        return pdfUris
                .stream()
                .flatMap(pdfUri -> getDossierSubjectsFromUri(URI.create(pdfUri))
                        .stream())
                .collect(Collectors.toList());
    }

    private List<Subject> getDossierSubjectsFromUri(URI pdfUri)  {
        List<Subject> subjects = new ArrayList<>();
        try (PDDocument document = PDDocument.load(getInputStreamFromUri(pdfUri))) {
            if (!document.isEncrypted()) {
                System.out.println("Processing: " + pdfUri);
                for (String candidate : getArrayOfStringFromPdf(document)) {
                    addSubjectFromStringToList(subjects, candidate, pdfUri.toString());
                }
                return subjects;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    private void addSubjectFromStringToList(List<Subject> subjects, String candidate, String pdfUri) {
        if (SubjectUtils.isStringMatched(candidate, SubjectUtils.REGEX_MATCHER_FOR_DOSSIER)) {
            Subject obtainedSubject = SubjectUtils.getSubjectFromString(candidate);
            obtainedSubject.setPdfUri(pdfUri);

            subjects.add(obtainedSubject);
        }
    }

    private String[] getArrayOfStringFromPdf(PDDocument document) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        return new PDFTextStripper().getText(document).split(SPLITTER_SYMBOL);
    }

    private InputStream getInputStreamFromUri(URI pdfUri) throws IOException {
        return uriParser.getStreamFromPdfUrl(pdfUri);
    }
}
