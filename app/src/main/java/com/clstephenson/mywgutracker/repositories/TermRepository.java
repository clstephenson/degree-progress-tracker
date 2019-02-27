package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.util.Pair;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.TermDao;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TermRepository implements Repository<Term> {

    private TermDao termDao;
    private LiveData<List<Term>> allTerms;
    private OnAsyncTaskResultListener onAsyncTaskResultListener;

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
        //todo not implemented
    }

    @Override
    public void delete(Term term) {
        new DeleteDataAsyncTask<Term>(termDao.getClass(), termDao).execute(Pair.create(term, onAsyncTaskResultListener));
    }

    @Override
    public void update(Term term) {
        new UpdateDataAsyncTask<Term>(termDao.getClass(), termDao).execute(Pair.create(term, onAsyncTaskResultListener));
        //termDao.update(term);
    }

    @Override
    public void insert(Term term) {
        new InsertDataAsyncTask<Term>(termDao.getClass(), termDao).execute(Pair.create(term, onAsyncTaskResultListener));
    }

    public void setOnAsyncTaskResultListener(OnAsyncTaskResultListener listener) {
        this.onAsyncTaskResultListener = listener;
    }
}
