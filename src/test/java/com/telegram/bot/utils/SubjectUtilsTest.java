package com.telegram.bot.utils;

import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.telegram.bot.domain.Subject;

@RunWith(MockitoJUnitRunner.class)
public class SubjectUtilsTest {

    @Test
    public void testGetSubjectFromStringShouldReturnCorrectSubject() {
        final String candidate = "ANDONII VITALII (97491/2016)";
        final Subject actual = SubjectUtils.getSubjectFromString(candidate);
        assertReflectionEquals(actual, Subject.builder().firstName("VITALII").lastName("ANDONII").id("97491/2016").build());
    }

    @Test
    public void testGetSubjectFromStringShouldReturnCorrectSubjectCorruptedString() {
        final String candidate = "ANDONII VITALII(97491/2016)";
        final Subject actual = SubjectUtils.getSubjectFromString(candidate);
        assertReflectionEquals(actual, Subject.builder().firstName("VITALII").lastName("ANDONII").id("97491/2016").build());
    }

    @Test
    public void testGetSubjectFromStringShouldReturnCorrectSubjectLeadingAndTrailingWhitespaces() {
        final String candidate = "     ANDONII VITALII (97491/2016)  ";
        final Subject actual = SubjectUtils.getSubjectFromString(candidate);
        assertReflectionEquals(actual, Subject.builder().firstName("VITALII").lastName("ANDONII").id("97491/2016").build());
    }

    @Test
    public void testGetSubjectFromStringShouldReturnCorrectSubjectWhitespaces() {
        final String candidate = "ANDONII    VITALII     (97491/2016)";
        final Subject actual = SubjectUtils.getSubjectFromString(candidate);
        assertReflectionEquals(actual, Subject.builder().firstName("VITALII").lastName("ANDONII").id("97491/2016").build());
    }

    @Test
    public void testGetSubjectFromStringShouldReturnCorrectSubjectWhitespacesAndCorruptedString() {
        final String candidate = "  ANDONII      VITALII(97491/2016)    ";
        final Subject actual = SubjectUtils.getSubjectFromString(candidate);
        assertReflectionEquals(actual, Subject.builder().firstName("VITALII").lastName("ANDONII").id("97491/2016").build());
    }

    @Test
    public void testIsStringMatchedShouldMatchDossier() {
        final String candidate = "ANDONII VITALII (97491/2016)";
        assertTrue(SubjectUtils.isStringMatched(candidate, SubjectUtils.REGEX_MATCHER_FOR_DOSSIER));
    }

    @Test
    public void testIsStringMatchedShouldMatchDossierWhenStringCorruptedWithWhitespaces() {
        final String candidate = "ANDONII    VITALII(97491/2016)";
        assertTrue(SubjectUtils.isStringMatched(candidate, SubjectUtils.REGEX_MATCHER_FOR_DOSSIER));
    }
}
