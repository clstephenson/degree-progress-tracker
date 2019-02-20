package com.clstephenson.mywgutracker;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final long MILLISECONDS_IN_DAY = 86_400_000;
    public static final int DATE_FORMAT_FLAGS =
            android.text.format.DateUtils.FORMAT_NUMERIC_DATE | android.text.format.DateUtils.FORMAT_SHOW_YEAR;

    public static String getFormattedDate(Context context, Date date) {
        return android.text.format.DateUtils.formatDateTime(context, date.getTime(), DATE_FORMAT_FLAGS);
    }

    public static String getFormattedDateRange(Context context, Date startDate, Date endDate) {
        return new StringBuilder()
                .append(getFormattedDate(context, startDate))
                .append(" - ")
                .append(getFormattedDate(context, endDate))
                .toString();
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isDateBeforeToday(Context context, Date date) {
        return date.before(getToday());
    }

    public static boolean isDateAfterToday(Context context, Date date) {
        return date.after(getToday());
    }

}
