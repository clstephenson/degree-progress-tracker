package com.clstephenson.mywgutracker.data.models;

import com.clstephenson.mywgutracker.data.CourseStatus;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "course",
        foreignKeys = {
                @ForeignKey(entity = Mentor.class, parentColumns = "id", childColumns = "mentor_id"),
                @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "term_id")
        },
        indices = {@Index("mentor_id"), @Index("term_id")}
)
public class Course {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

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

    @ColumnInfo(name = "mentor_id")
    private long mentorId;

    @ColumnInfo(name = "term_id")
    private long termId;

    @Ignore
    private List<Note> notes = Collections.emptyList();

    public Course(long id, @NonNull String name, Date startDate, Date endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, CourseStatus status, long mentorId,
                  long termId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isStartAlertOn = isStartAlertOn;
        this.isEndAlertOn = isEndAlertOn;
        this.status = status;
        this.mentorId = mentorId;
        this.termId = termId;
    }

    @Ignore
    public Course(@NonNull String name, Date startDate, Date endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, CourseStatus status, long mentorId,
                  long termId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isStartAlertOn = isStartAlertOn;
        this.isEndAlertOn = isEndAlertOn;
        this.status = status;
        this.mentorId = mentorId;
        this.termId = termId;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getMentorId() {
        return mentorId;
    }

    public void setMentorId(long mentorId) {
        this.mentorId = mentorId;
    }

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
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
                mentorId == course.mentorId &&
                termId == course.termId &&
                Objects.equals(name, course.name) &&
                Objects.equals(startDate, course.startDate) &&
                Objects.equals(endDate, course.endDate) &&
                status == course.status &&
                Objects.equals(notes, course.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate, isStartAlertOn, isEndAlertOn, status, mentorId, termId, notes);
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
                ", mentorId=" + mentorId +
                ", termId=" + termId +
                ", notes=" + notes +
                '}';
    }
}
