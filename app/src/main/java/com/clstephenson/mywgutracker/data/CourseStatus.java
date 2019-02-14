package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_status")
public class CourseStatus {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "status")
    private String mStatus;

    public CourseStatus(int id, @NonNull String status) {
        this.mId = id;
        this.mStatus = status;
    }

    public int getId() {
        return mId;
    }

    public String getStatus() {
        return mStatus;
    }
}
