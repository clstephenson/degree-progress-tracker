package com.clstephenson.mywgutracker.data.dao;

import com.clstephenson.mywgutracker.data.models.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class NoteDao implements BaseDao<Note> {

    @Query("SELECT * FROM note")
    public abstract LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note WHERE course_id = :courseId")
    public abstract LiveData<List<Note>> getNotesByCourseId(long courseId);

    @Query("SELECT * FROM note WHERE id = :noteId")
    public abstract Note getNoteById(long noteId);

}
