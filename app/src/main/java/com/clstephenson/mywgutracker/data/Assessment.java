package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(
        tableName = "assessment",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
        }
)
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "goal_date")
    private long goalDate;

    @ColumnInfo(name = "goal_alert_on")
    private boolean isGoalAlertOn;

    @ColumnInfo(name = "course_id")
    private int courseId;

    @ColumnInfo(name = "type")
    @TypeConverters(AssessmentTypeConverter.class)
    private AssessmentType type;

    public Assessment(int id, @NonNull String name, long goalDate, boolean isGoalAlertOn,
                      int courseId, AssessmentType type) {
        this.id = id;
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getGoalDate() {
        return goalDate;
    }

    public boolean isGoalAlertOn() {
        return isGoalAlertOn;
    }

    public int getCourseId() {
        return courseId;
    }

    public AssessmentType getType() {
        return type;
    }
}
