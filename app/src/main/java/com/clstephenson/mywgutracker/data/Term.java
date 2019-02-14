package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term")
public class Term {

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

    public Term(int id, @NonNull String name, long startDate, long endDate) {
        this.mId = id;
        this.mName = name;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
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
}
