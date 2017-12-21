package com.telegram.bot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subject {
    private String firstName;
    private String lastName;
    private String pdfUri;
    /* Id consists of an id of a dossier and a year when the dossier was submitted
     *
     *  Ex.: 85387/2016
     *  Where 85387 is id of dossier and 2016 is a year when the dossier was submitted
     * */
    private String id;
}
