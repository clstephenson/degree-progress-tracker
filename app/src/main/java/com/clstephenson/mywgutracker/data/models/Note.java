package com.clstephenson.mywgutracker.data.models;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "note",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("course_id")}
)
public class Note extends BaseModel {

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

    @Ignore
    public Note(Note note) {
        this(note.getId(), note.getNote(), note.getCourseId());
    }

    @NonNull
    public String getNote() {
        return note;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note1 = (Note) o;
        return id == note1.id &&
                courseId == note1.courseId &&
                Objects.equals(note, note1.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, courseId);
    }

    @Override
    public String toString() {
        int length = note.length() > 25 ? 25 : note.length();
        return String.format("%s...", note.substring(0, length));
    }
}
