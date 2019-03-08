package com.clstephenson.mywgutracker.utils;

import com.clstephenson.mywgutracker.ui.notifications.AlertNotification;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

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

    public static boolean isFirstDateBeforeSecond(String firstDate, String secondDate) {
        Date first = getDateFromFormattedString(firstDate);
        Date second = getDateFromFormattedString(secondDate);
        return first.before(second);
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
        }
        return null;

    }


    /**
     * @param date         original date on which the reminder is based
     * @param reminderDays number of days before the original date to set the reminder
     * @param hourOfDay    hour during the day for the reminder (24 hr clock and -1 means use current time)
     * @return
     */
    @Nullable
    public static Date createReminderDate(Date date, int reminderDays, int hourOfDay) {
        if (date != null) {
            Calendar now = Calendar.getInstance();
            int hours;
            int minutes;
            int seconds;
            if (hourOfDay == AlertNotification.REMINDER_USE_CURRENT_TIME_OF_DAY) {
                // need to get these so that the reminder can be set based on the current time
                // of day when created.  The date passed in is just the date, with zeros for time.
                hours = now.get(Calendar.HOUR_OF_DAY);
                minutes = now.get(Calendar.MINUTE);
                seconds = now.get(Calendar.SECOND);
            } else {
                hours = hourOfDay;
                minutes = 0;
                seconds = 0;
            }

            Calendar reminder = Calendar.getInstance();
            reminder.setTime(date);
            reminder.set(Calendar.HOUR_OF_DAY, hours);
            reminder.set(Calendar.MINUTE, minutes);
            reminder.set(Calendar.SECOND, seconds);
            reminder.add(Calendar.SECOND, 10); // added for ease of testing
            reminder.add(Calendar.DAY_OF_MONTH, 0 - reminderDays);
            return reminder.getTime();
        }
        return null;
    }
}
