package com.clstephenson.mywgutracker;

import com.clstephenson.mywgutracker.utils.ValidationUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidationUtilsTest {
    @Test
    public void isEmail_validatesTrue() {
        String email = "john@example.com";
        assertEquals(true, ValidationUtils.isEmail(email));
    }

    @Test
    public void isEmail_validatesFalse() {
        String email = "john@example";
        assertEquals(false, ValidationUtils.isEmail(email));
    }

    @Test
    public void isPhone_validatesTrueWithSevenDigitsOnly() {
        String phone = "1231234";
        assertEquals(true, ValidationUtils.isTelephone(phone));
    }

    @Test
    public void isPhone_validatesTrueWithSevenDigitsAndDash() {
        String phone = "123-1234";
        assertEquals(true, ValidationUtils.isTelephone(phone));
    }

    @Test
    public void isPhone_validatesTrueWithTenDigitsAndDashes() {
        String phone = "555-123-1234";
        assertEquals(true, ValidationUtils.isTelephone(phone));
    }

    @Test
    public void isPhone_validatesTrueWithTenDigitsAndParensAndDashes() {
        String phone = "(555) 123-1234";
        assertEquals(true, ValidationUtils.isTelephone(phone));
    }

    @Test
    public void isPhone_validatesFalseWithAlpha() {
        String phone = "asdefrt";
        assertEquals(false, ValidationUtils.isTelephone(phone));
    }
}