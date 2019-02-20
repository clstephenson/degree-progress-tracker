package com.clstephenson.mywgutracker.data;

import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class DataTypeConverter {

    @TypeConverter
    public static AssessmentType toAssessmentType(String type) {
        if (AssessmentType.valueOf(type) == AssessmentType.OBJECTIVE) {
            return AssessmentType.OBJECTIVE;
        } else if (AssessmentType.valueOf(type) == AssessmentType.PERFORMANCE) {
            return AssessmentType.PERFORMANCE;
        } else {
            throw new IllegalArgumentException("Could not recognize assessment type");
        }
    }

    @TypeConverter
    public static String fromAssessmentType(AssessmentType assessmentType) {
        return assessmentType.toString();
    }

    @TypeConverter
    public static CourseStatus toCourseStatus(String status) {
        if (CourseStatus.valueOf(status) == CourseStatus.COMPLETED) {
            return CourseStatus.COMPLETED;
        } else if (CourseStatus.valueOf(status) == CourseStatus.NOT_STARTED) {
            return CourseStatus.NOT_STARTED;
        } else if (CourseStatus.valueOf(status) == CourseStatus.STARTED) {
            return CourseStatus.STARTED;
        } else {
            throw new IllegalArgumentException("Could not recognize course status");
        }
    }

    @TypeConverter
    public static String fromCourseStatus(CourseStatus status) {
        return status.toString();
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
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

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
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
