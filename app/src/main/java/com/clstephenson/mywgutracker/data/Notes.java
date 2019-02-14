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
    private int id;

    @NonNull
    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "course_id")
    private int courseId;

    public Notes(int id, @NonNull String note, int courseId) {
        this.id = id;
        this.note = note;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public int getCourseId() {
        return courseId;
    }
}
