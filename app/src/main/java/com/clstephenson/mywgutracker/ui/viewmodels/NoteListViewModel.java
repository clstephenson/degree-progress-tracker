package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.repositories.NoteRepository;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NoteListViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> notes;
    private Note currentNote;

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
    }

    public LiveData<List<Note>> getNotes(long courseId) {
        notes = repository.getByCourseId(courseId);
        return notes;
    }

    public Note getNewNote(long courseId) {
        currentNote = new Note("", courseId);
        return currentNote;
    }

    public Note getNote(int position) {
        currentNote = notes.getValue().get(position);
        return currentNote;
    }

    public Note getNote() {
        return currentNote;
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void setDataTaskResultListener(OnDataTaskResultListener listener) {
        repository.setOnDataTaskResultListener(listener);
    }
}
