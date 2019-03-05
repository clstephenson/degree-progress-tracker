package com.clstephenson.mywgutracker.repositories;

import android.app.Application;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.TermDao;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TermRepository implements Repository<Term> {

    private TermDao termDao;
    private LiveData<List<Term>> allTerms;
    private OnDataTaskResultListener onDataTaskResultListener;

    public TermRepository(@NonNull Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        termDao = db.termDao();
        allTerms = termDao.getAll();
    }

    @Override
    public LiveData<List<Term>> getAll() {
        return allTerms;
    }

    @Override
    public LiveData<Term> getById(long id) {
        return termDao.getById(id);
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Term term) {
        new DataTask<Term>(termDao.getClass(), termDao, DataTask.Action.DELETE, onDataTaskResultListener).execute(term);
    }

    @Override
    public void update(Term term) {
        new DataTask<Term>(termDao.getClass(), termDao, DataTask.Action.UPDATE, onDataTaskResultListener).execute(term);
    }

    @Override
    public void insert(Term term) {
        new DataTask<Term>(termDao.getClass(), termDao, DataTask.Action.INSERT, onDataTaskResultListener).execute(term);
    }

    public void setOnDataTaskResultListener(OnDataTaskResultListener listener) {
        this.onDataTaskResultListener = listener;
    }
}
