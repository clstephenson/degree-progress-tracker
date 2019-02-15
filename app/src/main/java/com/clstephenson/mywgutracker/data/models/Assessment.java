package com.clstephenson.mywgutracker.data.models;

import com.clstephenson.mywgutracker.data.AssessmentType;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "assessment",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
        },
        indices = {@Index("course_id")}
)
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "code")
    private String code;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "goal_date")
    private Date goalDate;

    @ColumnInfo(name = "goal_alert_on")
    private boolean isGoalAlertOn;

    @ColumnInfo(name = "course_id")
    private long courseId;

    @ColumnInfo(name = "type")
    private AssessmentType type;

    public Assessment(long id, String code, @NonNull String name, Date goalDate, boolean isGoalAlertOn,
                      long courseId, AssessmentType type) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.type = type;
    }

    @Ignore
    public Assessment(String code, @NonNull String name, Date goalDate, boolean isGoalAlertOn,
                      long courseId, AssessmentType type) {
        this.code = code;
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public boolean isGoalAlertOn() {
        return isGoalAlertOn;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public AssessmentType getType() {
        return type;
    }

    public Date getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(@NonNull Date goalDate) {
        this.goalDate = goalDate;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setGoalAlertOn(boolean goalAlertOn) {
        isGoalAlertOn = goalAlertOn;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }
}
