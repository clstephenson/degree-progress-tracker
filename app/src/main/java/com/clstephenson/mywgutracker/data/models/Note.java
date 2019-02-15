package com.clstephenson.mywgutracker.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "note",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
        },
        indices = {@Index("course_id")}
)
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "course_id")
    private long courseId;

    public Note(long id, @NonNull String note, long courseId) {
        this.id = id;
        this.note = note;
        this.courseId = courseId;
    }

    @Ignore
    public Note(@NonNull String note, long courseId) {
        this.note = note;
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }
}
