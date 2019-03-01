package com.clstephenson.mywgutracker;

import android.content.Context;

import com.clstephenson.mywgutracker.utils.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    Context context;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFormattedDate() {
        Date date = createDate(2019, 1, 15);
        assertEquals("2/15/2019", DateUtils.getFormattedDate(date));
    }

    @Test
    public void getFormattedDateRange() {
        Date date1 = createDate(2019, 1, 15);
        Date date2 = createDate(2019, 5, 20);
        assertEquals("2/15/2019 - 6/20/2019", DateUtils.getFormattedDateRange(date1, date2));
    }

    private Date createDate(int year, int zeroBasedMonth, int dayOfMoth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(year, zeroBasedMonth, dayOfMoth);
        return cal.getTime();
    }
}