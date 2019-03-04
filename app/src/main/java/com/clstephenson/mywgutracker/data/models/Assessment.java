package com.clstephenson.mywgutracker.data.models;

import com.clstephenson.mywgutracker.data.AssessmentType;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "assessment",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("course_id")}
)
public class Assessment extends BaseModel {

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

    //todo create copy constructor

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return isGoalAlertOn == that.isGoalAlertOn &&
                courseId == that.courseId &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(goalDate, that.goalDate) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, goalDate, isGoalAlertOn, courseId, type);
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", goalDate=" + goalDate +
                ", isGoalAlertOn=" + isGoalAlertOn +
                ", courseId=" + courseId +
                ", type=" + type +
                '}';
    }
}
