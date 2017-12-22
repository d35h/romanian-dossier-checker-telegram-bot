package com.telegram.bot.utils;

import com.telegram.bot.domain.Subject;

public class MessageUtils {

    public static final String NOT_ON_THE_LIST_MESSAGE = "You are not on the list, but no worries, try later.";

    public static final String START_COMMAND = "/start";

    public static final String WELCOMING_MESSAGE = "Willkommen doamna sau domnule, \n\nI am here to check the state of your dossier." +
            "\n\nJust type your ID (dossier number: number/year, Ex.: 1763/2013) below:";

    public static String generateLookupMessage(String lookupValue) {
        return "I am looking for a dossier by ID: " + lookupValue
                + "\nMostly it takes up to 7 minutes. Make yourself a cup of coffee :).";
    }

    public static String generateSuccessfulLookupMessage(Subject foundSubjects) {
        return "We have got good news for you!\nYou are on the list!\nSee the details: \n" + foundSubjects + ".\nCool, huh?";
    }
}
