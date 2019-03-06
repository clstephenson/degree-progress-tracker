package com.clstephenson.mywgutracker.repositories;

import android.app.Application;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.NoteDao;
import com.clstephenson.mywgutracker.data.models.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class NoteRepository implements Repository<Note> {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private OnDataTaskResultListener onDataTaskResultListener;

    public NoteRepository(@NonNull Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAll();
    }

    @Override
    public LiveData<List<Note>> getAll() {
        return allNotes;
    }

    @Override
    public LiveData<Note> getById(long id) {
        return noteDao.getById(id);
    }

    public LiveData<List<Note>> getByCourseId(long courseId) {
        return noteDao.getByCourseId(courseId);
    }

    @Override
    public void deleteById(long id) {
        noteDao.deleteById(id);
    }

    @Override
    public void delete(Note note) {
        new DataTask<Note>(noteDao.getClass(), noteDao, DataTask.Action.DELETE, onDataTaskResultListener).execute(note);
    }

    @Override
    public void update(Note note) {
        new DataTask<Note>(noteDao.getClass(), noteDao, DataTask.Action.UPDATE, onDataTaskResultListener).execute(note);
    }

    @Override
    public void insert(Note note) {
        new DataTask<Note>(noteDao.getClass(), noteDao, DataTask.Action.INSERT, onDataTaskResultListener).execute(note);
    }

    public void setOnDataTaskResultListener(OnDataTaskResultListener listener) {
        this.onDataTaskResultListener = listener;
    }

}
