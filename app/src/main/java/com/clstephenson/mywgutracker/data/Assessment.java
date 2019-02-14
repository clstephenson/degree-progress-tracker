package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "assessment",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id"),
                @ForeignKey(entity = AssessmentType.class, parentColumns = "id", childColumns = "type_id")
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

    @ColumnInfo(name = "type_id")
    private int typeId;

    public Assessment(int id, @NonNull String name, long goalDate, boolean isGoalAlertOn,
                      int courseId, int typeId) {
        this.id = id;
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.typeId = typeId;
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

    public int getTypeId() {
        return typeId;
    }
}
