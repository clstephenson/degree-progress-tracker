package com.clstephenson.mywgutracker.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.NoteDao;
import com.clstephenson.mywgutracker.data.models.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class NoteRepository implements Repository<Note> {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

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
    public Note getById(long id) {
        return noteDao.getById(id);
    }

    @Override
    public void delete(Note note) {
        noteDao.delete(note);
    }

    @Override
    public void deleteAll() {
        noteDao.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        noteDao.deleteById(id);
    }

    @Override
    public void update(Note note) {
        noteDao.update(note);
    }

    @Override
    public void insert(Note note) {
        new NoteRepository.insertAsyncTask(noteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao asyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
