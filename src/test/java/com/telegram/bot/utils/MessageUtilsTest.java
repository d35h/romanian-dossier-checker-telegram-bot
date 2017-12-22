package com.telegram.bot.utils;

import static com.telegram.bot.domain.Subject.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.telegram.bot.domain.Subject;

@RunWith(MockitoJUnitRunner.class)
public class MessageUtilsTest {

    @Test
    public void testGenerateLookupMessageShouldReturnCorrectMessage() {
        final String actual = MessageUtils.generateLookupMessage("lookupValue");
        final String expected = "I am looking for a dossier by ID: lookupValue\nMostly it takes up to 7 minutes. Make yourself a cup of coffee :).";
        assertThat(actual, is(expected));
    }

    @Test
    public void testGenerateSuccessfulLookupMessageShouldReturnCorrectMessage() {
        final Subject subject = builder().firstName("FirstnameTest1").lastName("LastnameTest1").id("IdTest").pdfUri("PdfTest1").build();
        final String expected = "We have got good news for you!\nYou are on the list!\nSee the details: \nSubject(firstName=FirstnameTest1, lastName=LastnameTest1, pdfUri=PdfTest1, id=IdTest).\nCool, huh?";
        assertThat(MessageUtils.generateSuccessfulLookupMessage(subject), is(expected));
    }
}
