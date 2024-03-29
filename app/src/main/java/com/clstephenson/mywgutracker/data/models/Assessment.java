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

    public Assessment(long id, @NonNull String name, @NonNull Date goalDate, boolean isGoalAlertOn,
                      long courseId, AssessmentType type) {
        this.id = id;
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.type = type;
    }

    @Ignore
    public Assessment(@NonNull String name, @NonNull Date goalDate, boolean isGoalAlertOn,
                      long courseId, AssessmentType type) {
        this.name = name;
        this.goalDate = goalDate;
        this.isGoalAlertOn = isGoalAlertOn;
        this.courseId = courseId;
        this.type = type;
    }

    @Ignore
    public Assessment(Assessment assessment) {
        this(assessment.getId(), assessment.getName(), assessment.getGoalDate(),
                assessment.isGoalAlertOn(), assessment.getCourseId(), assessment.getType());
    }

    @NonNull
    public String getName() {
        return name;
    }

    public boolean isGoalAlertOn() {
        return isGoalAlertOn;
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
        return id == that.id &&
                isGoalAlertOn == that.isGoalAlertOn &&
                courseId == that.courseId &&
                Objects.equals(name, that.name) &&
                Objects.equals(goalDate, that.goalDate) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, goalDate, isGoalAlertOn, courseId, type);
    }

    @Override
    public String toString() {
        return name;
    }
}
