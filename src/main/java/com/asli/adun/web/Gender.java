package com.asli.adun.web;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Allan G. Ramirez (ramirezag@gmail.com)
 */
public enum Gender {
    MALE("male"), FEMALE("female"), BOTH("both");
    String text;

    Gender(String text) {
        this.text = text;
    }

    @JsonCreator
    public static Gender fromString(String string) {
        if ("male".equalsIgnoreCase(string)) {
            return MALE;
        } else if ("female".equalsIgnoreCase(string)) {
            return FEMALE;
        } else if ("both".equalsIgnoreCase(string)) {
            return BOTH;
        } else {
            throw new IllegalArgumentException(string + " has no corresponding value");
        }
    }
}