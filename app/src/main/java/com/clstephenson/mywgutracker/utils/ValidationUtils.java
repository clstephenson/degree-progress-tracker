package com.clstephenson.mywgutracker.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    public static boolean isEndBeforeStart(String startDate, String endDate) {
        return DateUtils.isFirstDateBeforeSecond(endDate, startDate);
    }

    public static boolean isTelephone(String input) {
        if (!isEmpty(input)) {
            if (input.matches(Patterns.PHONE.pattern())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmail(String input) {
        if (!isEmpty(input)) {
            if (input.matches(Patterns.EMAIL_ADDRESS.pattern())) {
                return true;
            }
        }
        return false;
    }
}
