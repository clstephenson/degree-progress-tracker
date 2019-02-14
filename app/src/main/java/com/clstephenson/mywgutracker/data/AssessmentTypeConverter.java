package com.clstephenson.mywgutracker.data;

import androidx.room.TypeConverter;

public class AssessmentTypeConverter {

    @TypeConverter
    public static AssessmentType toType(String type) {
        if (type.equals(AssessmentType.OBJECTIVE.toString())) {
            return AssessmentType.OBJECTIVE;
        } else if (type.equals(AssessmentType.PERFORMANCE)) {
            return AssessmentType.PERFORMANCE;
        } else {
            throw new IllegalArgumentException("Could not recognize assessment type");
        }
    }
}
