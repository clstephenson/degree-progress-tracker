package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "start_date")
    private long mStartDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private long mEndDate;

    @ColumnInfo(name = "start_alert_on")
    private boolean mIsStartAlertOn;

    @ColumnInfo(name = "end_alert_on")
    private boolean mIsEndAlertOn;

    @ColumnInfo(name = "status_id")
    private int mStatusId;

    @ColumnInfo(name = "mentor_id")
    private int mMentorId;

    @ColumnInfo(name = "term_id")
    private int mTermId;

    public Course(int id, @NonNull String name, long startDate, long endDate,
                  boolean isStartAlertOn, boolean isEndAlertOn, int statusId, int mentorId,
                  int mTermId) {
        this.mId = id;
        this.mName = name;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mIsStartAlertOn = isStartAlertOn;
        this.mIsEndAlertOn = isEndAlertOn;
        this.mStatusId = statusId;
        this.mMentorId = mentorId;
        this.mTermId = mTermId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public boolean isIsStartAlertOn() {
        return mIsStartAlertOn;
    }

    public boolean isIsEndAlertOn() {
        return mIsEndAlertOn;
    }

    public int getStatusId() {
        return mStatusId;
    }

    public int getMentorId() {
        return mMentorId;
    }

    public int getTermId() {
        return mTermId;
    }
}
