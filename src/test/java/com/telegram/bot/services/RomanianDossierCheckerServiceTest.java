package com.telegram.bot.services;

import static com.telegram.bot.domain.Subject.*;
import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.unitils.reflectionassert.ReflectionAssert.*;

import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import com.telegram.bot.domain.Subject;
import com.telegram.bot.services.parsers.WebPageParser;

@RunWith(MockitoJUnitRunner.class)
public class RomanianDossierCheckerServiceTest {

    @Mock
    private SubjectHandler subjectHandler;

    @Mock
    private WebPageParser webPageParser;

    @InjectMocks
    private RomanianDossierCheckerService service;

    private List<String> pdfLinks = asList("link1", "link2");

    private static int ID = 1;

    @Before
    public void init() {
        when(webPageParser.getPdfLinks()).thenReturn(pdfLinks);
        when(subjectHandler.getDossierSubjectsByUri(any(URI.class))).thenReturn(getSubjects(ID));
    }

    @Test
    public void testFindSubjectByDossierIdShouldReturnSubject() {
        final Subject actual = service.findSubjectByDossierId("IdTest1");
        final Subject expected = builder().firstName("FirstnameTest1").lastName("LastnameTest1").id("IdTest1").pdfUri("PdfTest1").build();
        assertReflectionEquals(actual, expected);
        verify(webPageParser).getPdfLinks();
        verify(subjectHandler).getDossierSubjectsByUri(any(URI.class));
        verifyNoMoreInteractions(webPageParser, subjectHandler);
    }

    @Test
    public void testFindSubjectByDossierIdShouldNotReturnSubject() {
        final Subject actual = service.findSubjectByDossierId("IdTest5");
        assertNull(actual);
        verify(webPageParser).getPdfLinks();
        verify(subjectHandler, times(2)).getDossierSubjectsByUri(any(URI.class));
        verifyNoMoreInteractions(webPageParser, subjectHandler);
    }

    @Test
    public void testPrepareMessageShouldReturnCorrectSendMessageObject() {
        final String messageText = "MESSAGE_TEST";
        assertReflectionEquals(service.prepareMessage(1, messageText), new SendMessage(1L, messageText));
    }

    @Test
    public void testGetResponseMessageShouldReturnCorrectMessageForSuccessfulCase() {
        Subject subject = builder().firstName("FirstnameTest1").lastName("LastnameTest1").id("IdTest").pdfUri("PdfTest1").build();
        String actual = service.getResponseMessage(subject);
        String expected = "We have got good news for you!\nYou are on the list!\nSee the details: \n" + subject + ".\nCool, huh?";
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetResponseMessageShouldReturnCorrectMessageForNegativeCase() {
        Subject subject = null;
        String actual = service.getResponseMessage(subject);
        String expected = "You are not on the list, but no worries, try later.";
        assertThat(actual, is(expected));
    }

    private List<Subject> getSubjects(int id) {
        return asList(
                builder().firstName("FirstnameTest1").lastName("LastnameTest1").id("IdTest" + id).pdfUri("PdfTest1").build(),
                builder().firstName("FirstnameTest2").lastName("LastnameTest2").id("IdTest" + ++id).pdfUri("PdfTest2").build(),
                builder().firstName("FirstnameTest3").lastName("LastnameTest3").id("IdTest" + ++id).pdfUri("PdfTest3").build()
        );
    }
}
