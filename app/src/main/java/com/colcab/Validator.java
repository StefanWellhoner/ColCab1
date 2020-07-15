package com.colcab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static final Pattern patFullName = Pattern.compile("^[A-Z][a-z]");

    public static boolean valFullName(String fullName) {
        Matcher matcher = patFullName.matcher(fullName);
        return matcher.find();
    }

}
