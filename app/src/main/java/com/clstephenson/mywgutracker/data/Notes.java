package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "note",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
        }
)
public class Notes {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "note")
    private String mNote;

    @ColumnInfo(name = "course_id")
    private int mCourseId;

    public Notes(int id, @NonNull String note, int courseId) {
        this.mId = id;
        this.mNote = note;
        this.mCourseId = courseId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getNote() {
        return mNote;
    }

    public int getCourseId() {
        return mCourseId;
    }
}
