package com.clstephenson.mywgutracker.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.TermDao;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TermRepository implements Repository<Term> {

    private TermDao termDao;
    private LiveData<List<Term>> allTerms;

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
    public Term getById(long id) {
        return termDao.getById(id);
    }

    @Override
    public void delete(Term term) {
        termDao.delete(term);
    }

    @Override
    public void deleteAll() {
        termDao.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        termDao.deleteById(id);
    }

    @Override
    public void update(Term term) {
        termDao.update(term);
    }

    @Override
    public void insert(Term term) {
        new TermRepository.insertAsyncTask(termDao).execute(term);
    }

    private static class insertAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao asyncTaskDao;

        insertAsyncTask(TermDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Term... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
