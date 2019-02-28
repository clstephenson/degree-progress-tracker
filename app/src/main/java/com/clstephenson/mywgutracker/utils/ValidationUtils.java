package com.clstephenson.mywgutracker.utils;

import android.text.TextUtils;

public class ValidationUtils {

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    public static boolean isEndBeforeStart(String startDate, String endDate) {
        return DateUtils.isFirstDateBeforeSecond(endDate, startDate);
    }
}
