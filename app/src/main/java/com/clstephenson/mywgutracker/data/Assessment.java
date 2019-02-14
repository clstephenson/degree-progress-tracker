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
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "goal_date")
    private long mGoalDate;

    @ColumnInfo(name = "goal_alert_on")
    private boolean mIsGoalAlertOn;

    @ColumnInfo(name = "course_id")
    private int mCourseId;

    @ColumnInfo(name = "type_id")
    private int mTypeId;

    public Assessment(int id, @NonNull String name, long goalDate, boolean isGoalAlertOn,
                      int courseId, int typeId) {
        this.mId = id;
        this.mName = name;
        this.mGoalDate = goalDate;
        this.mIsGoalAlertOn = isGoalAlertOn;
        this.mCourseId = courseId;
        this.mTypeId = typeId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public long getGoalDate() {
        return mGoalDate;
    }

    public boolean isIsGoalAlertOn() {
        return mIsGoalAlertOn;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public int getTypeId() {
        return mTypeId;
    }
}
