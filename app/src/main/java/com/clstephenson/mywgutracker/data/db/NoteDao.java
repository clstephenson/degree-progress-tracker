package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class NoteDao implements BaseDao<Note> {

    @Query("SELECT * FROM note")
    public abstract LiveData<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE course_id = :courseId")
    public abstract LiveData<List<Note>> getByCourseId(long courseId);

    @Query("SELECT * FROM note WHERE id = :noteId")
    public abstract Note getById(long noteId);

    @Query("DELETE FROM note")
    public abstract void deleteAll();

    @Query("DELETE FROM note WHERE id = :id")
    public abstract void deleteById(long id);

}
