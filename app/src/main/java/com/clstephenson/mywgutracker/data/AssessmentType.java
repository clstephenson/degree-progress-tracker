package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_type")
public class AssessmentType {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "type")
    private String mType;

    public AssessmentType(int id, @NonNull String type) {
        this.mId = id;
        this.mType = type;
    }

    public int getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }
}
