package com.clstephenson.mywgutracker.data.models;

import com.clstephenson.mywgutracker.data.CourseStatus;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "course",
        foreignKeys = {
                @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "term_id")
        },
        indices = {@Index("term_id")}
)
public class Course extends BaseModel {

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "start_date")
    private Date startDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate;

    @ColumnInfo(name = "start_alert_on")
    private boolean isStartAlertOn;

    @ColumnInfo(name = "end_alert_on")
    private boolean isEndAlertOn;

    @ColumnInfo(name = "status")
    private CourseStatus status;

    @Embedded
    private Mentor mentor;

    @ColumnInfo(name = "term_id")
    private long termId;

    @Ignore
    private List<Assessment> assessments = Collections.emptyList();

    @Ignore
    private List<Note> notes = Collections.emptyList();

    public Course(long id, @NonNull String name, Date startDate, Date endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, CourseStatus status, Mentor mentor,
                  long termId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isStartAlertOn = isStartAlertOn;
        this.isEndAlertOn = isEndAlertOn;
        this.status = status;
        this.mentor = mentor;
        this.termId = termId;
    }

    @Ignore
    public Course(@NonNull String name, Date startDate, Date endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, CourseStatus status, Mentor mentor,
                  long termId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isStartAlertOn = isStartAlertOn;
        this.isEndAlertOn = isEndAlertOn;
        this.status = status;
        this.mentor = mentor;
        this.termId = termId;
    }

    /**
     * Copy constructor
     *
     * @param course
     */
    @SuppressWarnings("JavaDoc")
    @Ignore
    public Course(Course course) {
        this(course.getId(), course.getName(), course.getStartDate(), course.getEndDate(),
                course.isStartAlertOn(), course.isEndAlertOn(), course.getStatus(),
                new Mentor(course.getMentor()), course.getTermId());
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean isStartAlertOn() {
        return isStartAlertOn;
    }

    public boolean isEndAlertOn() {
        return isEndAlertOn;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setStartAlertOn(boolean startAlertOn) {
        isStartAlertOn = startAlertOn;
    }

    public void setEndAlertOn(boolean endAlertOn) {
        isEndAlertOn = endAlertOn;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return isStartAlertOn == course.isStartAlertOn &&
                isEndAlertOn == course.isEndAlertOn &&
                mentor == course.mentor &&
                termId == course.termId &&
                Objects.equals(id, course.id) &&
                Objects.equals(name, course.name) &&
                Objects.equals(startDate, course.startDate) &&
                Objects.equals(endDate, course.endDate) &&
                status == course.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, isStartAlertOn, isEndAlertOn, status, mentor, termId);
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isStartAlertOn=" + isStartAlertOn +
                ", isEndAlertOn=" + isEndAlertOn +
                ", status=" + status +
                ", termId=" + termId +
                '}';
    }
}
