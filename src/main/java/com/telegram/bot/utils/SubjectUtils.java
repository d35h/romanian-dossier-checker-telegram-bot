package com.telegram.bot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.telegram.bot.domain.Subject;

public class SubjectUtils {

    private final static int INDEX_OF_SUBJECT_LASTNAME = 0;

    private final static int INDEX_OF_SUBJECT_FIRSTNAME = 1;

    private final static int INDEX_OF_SUBJECT_ID = 2;

    private final static int MAX_CORRECT_NUMBER_OF_VALUES = 3;

    private final static String REGEX_MATCHER_FOR_DOSSIER_LAST_FIRST_NAMES = "[\\W|\\w]+\\s+[\\W|\\w]+";

    private final static String REGEX_MATCHER_FOR_DOSSIER_ID = "\\(\\d+\\/\\d+\\)\\s*$";

    public final static String REGEX_MATCHER_FOR_DOSSIER = REGEX_MATCHER_FOR_DOSSIER_LAST_FIRST_NAMES + REGEX_MATCHER_FOR_DOSSIER_ID;

    private final static String REGEX_REPLACE_PARENTHESES = "\\(|\\)";

    private final static String REGEX_REPLACE_LEADING_AND_TRAILING_WHITESPACES = "^[\\s]+|\\s+$";

    private final static String SPLITTER_FOR_SUBJECT = "\\s{1,}|\\(";

    public static Subject getSubjectFromString(String candidate) {
        final List<String> listWithSubjectValues = getListWithSubjectValues(candidate);

        return Subject.builder()
                .lastName(listWithSubjectValues.get(INDEX_OF_SUBJECT_LASTNAME))
                .firstName(listWithSubjectValues.get(INDEX_OF_SUBJECT_FIRSTNAME))
                .id(listWithSubjectValues.get(INDEX_OF_SUBJECT_ID))
                .build();
    }

    private static List<String> getListWithSubjectValues(String subjectString) {
        subjectString = subjectString.replaceAll(REGEX_REPLACE_LEADING_AND_TRAILING_WHITESPACES, StringUtils.EMPTY);
        return Lists.newArrayList(subjectString.split(SPLITTER_FOR_SUBJECT, MAX_CORRECT_NUMBER_OF_VALUES))
                .stream()
                .map(SubjectUtils::replaceAllParentheses)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean isStringMatched(String candidate, String regEx) {
        return Pattern.compile(regEx).matcher(candidate).matches();
    }

    private static String replaceAllParentheses(String source) {
        return replaceAll(source, REGEX_REPLACE_PARENTHESES, StringUtils.EMPTY);
    }

    private static String replaceAll(String source, String regEx, String replaceBy) {
        return source.replaceAll(regEx, replaceBy);
    }
}
