package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
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
    private OnAsyncTaskResultListener delegate;

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
    public void deleteById(long id) throws EntityConstraintException {
        new deleteByIdAsyncTask(termDao).execute(Pair.create(id, delegate));
    }

    @Override
    public void update(Term term) {
        termDao.update(term);
    }

    @Override
    public void insert(Term term) {
        new TermRepository.insertAsyncTask(termDao).execute(term);
    }

    public void setOnAsyncTaskResultListener(OnAsyncTaskResultListener delegate) {
        this.delegate = delegate;
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

    private static class deleteByIdAsyncTask extends AsyncTask<Pair<Long, OnAsyncTaskResultListener>, Void, AsyncTaskResult> {
        AsyncTaskResult result;
        OnAsyncTaskResultListener delegate;
        private TermDao asyncTaskDao;

        deleteByIdAsyncTask(TermDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected AsyncTaskResult doInBackground(final Pair<Long, OnAsyncTaskResultListener>... params) {
            try {
                Long id = params[0].first;
                delegate = params[0].second;
                asyncTaskDao.deleteById(id);
                result = new AsyncTaskResult(AsyncTaskResult.Result.SUCCESS);
            } catch (SQLiteConstraintException e) {
                EntityConstraintException constraintException = new EntityConstraintException(e);
                result = new AsyncTaskResult(AsyncTaskResult.Result.FAIL, constraintException);
            }
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult taskResult) {
            delegate.onAsyncTaskResult(result);
        }
    }
}
