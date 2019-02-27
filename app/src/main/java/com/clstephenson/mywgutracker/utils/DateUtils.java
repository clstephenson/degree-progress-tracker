package com.clstephenson.mywgutracker.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final long MILLISECONDS_IN_DAY = 86_400_000;
    public static final String DATE_FORMAT = "MMM d, yyyy";

    public static DateFormat getDateFormatter() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

    public static Date getDateFromFormattedString(String formattedDate) {
        Date date;
        try {
            date = getDateFormatter().parse(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static String getFormattedDate(Date date) {
        return getDateFormatter().format(date);
    }

    public static String getFormattedDateRange(Date startDate, Date endDate) {
        return new StringBuilder()
                .append(getFormattedDate(startDate))
                .append(" - ")
                .append(getFormattedDate(endDate))
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

    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isDateBeforeToday(Date date) {
        return date.before(getToday());
    }

    public static boolean isDateAfterToday(Date date) {
        return date.after(getToday());
    }


    public static Date getDatefromMillis(Long value) {
        if (value != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } else {
            return null;
        }
    }


    public static Long getMillisFromDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        } else {
            return null;
        }
    }
}
