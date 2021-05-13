package com.example.quixote_login.Utilities;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String PASSWORD_PATTERN = "^(?=[a-z])(?=(?:.*[A-Z]){2})(?=(?:.*[0-9]){2})(?=.*[?!.@$])[a-zA-Z0-9?!.@$]{8,15}$";
    private static Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    private static final String PHONE_NUMBER_PATTERN = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
    private static Pattern phonePattern = Pattern.compile(PHONE_NUMBER_PATTERN);

    private static final String EMAIL_PATTERN = "^[A-Z0-9a-z._%+-]{4,25}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validateEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find() && matcher.group().equals(email);
    }

    public static boolean validatePassword(String password){
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.find() && matcher.group().equals(password);
    }

    public static boolean validatePhone(String phone){
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.find() && matcher.group().equals(phone);
    }

    public static boolean validateTitle(String title){
        return (title.length() <= 100 && title.length() >= 5);
    }
    public static boolean validateDescription(String description){
        return (description.length() <= 1000 && description.length() >= 100);
    }
}
