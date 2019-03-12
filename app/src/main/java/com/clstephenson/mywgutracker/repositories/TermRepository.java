package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.TermDao;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TermRepository implements Repository<Term> {

    private final TermDao termDao;
    private final LiveData<List<Term>> allTerms;
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

    public void getAllAsList() {
        new GetAllTask(termDao, onDataTaskResultListener).execute();
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

    static class GetAllTask extends AsyncTask<Void, Void, DataTaskResult> {

        private final String TAG = this.getClass().getSimpleName();
        private final OnDataTaskResultListener listener;
        private final TermDao asyncTaskDao;
        private final com.clstephenson.mywgutracker.repositories.DataTask.Action action;

        GetAllTask(TermDao dao, OnDataTaskResultListener listener) {
            this.action = com.clstephenson.mywgutracker.repositories.DataTask.Action.GET;
            asyncTaskDao = dao;
            this.listener = listener;
        }

        @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
        @Override
        protected DataTaskResult doInBackground(final Void... params) {
            DataTaskResult dataTaskResult = new DataTaskResult(action, DataTaskResult.Result.FAIL);
            try {
                List<Term> terms = asyncTaskDao.getAllAsList();
                dataTaskResult.setResult(DataTaskResult.Result.SUCCESS);
                dataTaskResult.setData(terms);
                return dataTaskResult;
            } catch (SQLiteConstraintException e) {
                Log.w(TAG, "doInBackground: " + e.getLocalizedMessage(), e);
                dataTaskResult.setConstraintException(new EntityConstraintException(e));
            } catch (Exception e) {
                Log.w(TAG, "doInBackground: " + e.getLocalizedMessage(), e);
                dataTaskResult.setOtherException(e);
            } finally {
                return dataTaskResult;
            }
        }

        @Override
        protected void onPostExecute(DataTaskResult taskResult) {
            listener.onNotifyDataChanged(taskResult);
        }
    }
}
