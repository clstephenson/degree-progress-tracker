package com.clstephenson.mywgutracker.data;

import androidx.room.TypeConverter;

public class CourseStatusTypeConverter {

    @TypeConverter
    public static CourseStatus toStatus(String type) {
        if (type.equals(CourseStatus.COMPLETED.toString())) {
            return CourseStatus.COMPLETED;
        } else if (type.equals(CourseStatus.NOT_STARTED)) {
            return CourseStatus.NOT_STARTED;
        } else if (type.equals(CourseStatus.STARTED)) {
            return CourseStatus.STARTED;
        } else {
            throw new IllegalArgumentException("Could not recognize course status");
        }
    }
}
