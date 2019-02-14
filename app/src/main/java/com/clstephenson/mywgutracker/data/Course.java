package com.clstephenson.mywgutracker.data;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "course",
        foreignKeys = {
                @ForeignKey(entity = CourseStatus.class, parentColumns = "id", childColumns = "status_id"),
                @ForeignKey(entity = Mentor.class, parentColumns = "id", childColumns = "mentor_id"),
                @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "term_id")
        }
)
public class Course {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "start_date")
    private long startDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private long endDate;

    @ColumnInfo(name = "start_alert_on")
    private boolean isStartAlertOn;

    @ColumnInfo(name = "end_alert_on")
    private boolean isEndAlertOn;

    @ColumnInfo(name = "status_id")
    private int statusId;

    @ColumnInfo(name = "mentor_id")
    private int mentorId;

    @ColumnInfo(name = "term_id")
    private int termId;

    @Ignore
    private List<Notes> notes = Collections.emptyList();

    public Course(int id, @NonNull String name, long startDate, long endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, int statusId, int mentorId,
                  int termId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isStartAlertOn = isStartAlertOn;
        this.isEndAlertOn = isEndAlertOn;
        this.statusId = statusId;
        this.mentorId = mentorId;
        this.termId = termId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public boolean isStartAlertOn() {
        return isStartAlertOn;
    }

    public boolean isEndAlertOn() {
        return isEndAlertOn;
    }

    public int getStatusId() {
        return statusId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public int getTermId() {
        return termId;
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }
}
