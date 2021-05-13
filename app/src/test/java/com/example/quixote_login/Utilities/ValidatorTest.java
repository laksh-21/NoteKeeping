package com.example.quixote_login.Utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void validateEmail() {
        String email = "abcdef";
        boolean result = Validator.validateEmail(email);
        assertEquals(false, result);

        email = "as@gmail.com";
        assertEquals(false, Validator.validateEmail(email));

        email = "iamlakshable@gmail.com";
        assertEquals(true, Validator.validateEmail(email));
    }

    @Test
    public void validatePassword() {
        String password = "password";
        assertEquals(false, Validator.validatePassword(password));

        password = "aaAA11..";
        assertEquals(true, Validator.validatePassword(password));

        password = "kOD11.";
        assertEquals(false, Validator.validatePassword(password));
    }

    @Test
    public void validatePhone() {
        String phone = "+918126681065";
        assertEquals(true, Validator.validatePhone(phone));

        phone = "7375981793";
        assertEquals(true, Validator.validatePhone(phone));

        phone = "12345";
        assertEquals(false, Validator.validatePhone(phone));
    }
}