package com.clstephenson.mywgutracker;

import com.clstephenson.mywgutracker.utils.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFormattedDate() {
        Date date = createDate(2019, 1, 15);
        assertEquals("Feb 15, 2019", DateUtils.getFormattedDate(date));
    }

    @Test
    public void getFormattedDateRange() {
        Date date1 = createDate(2019, 1, 15);
        Date date2 = createDate(2019, 5, 20);
        assertEquals("Feb 15, 2019 - Jun 20, 2019", DateUtils.getFormattedDateRange(date1, date2));
    }

    @Test
    public void createReminderDate() {
        Date date = createDate(2019, 2, 7);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 28);
        cal.add(Calendar.SECOND, 15);
        Date reminderDate = cal.getTime();
        assertEquals(reminderDate.getTime(), DateUtils.createReminderDate(date, 7).getTime());
    }

    private Date createDate(int year, int zeroBasedMonth, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, zeroBasedMonth, dayOfMonth);
        return cal.getTime();
    }
}