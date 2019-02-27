package com.clstephenson.mywgutracker.repositories;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Pair;

import com.clstephenson.mywgutracker.data.db.BaseDao;
import com.clstephenson.mywgutracker.data.models.BaseModel;

/**
 * @param <T> The entity model class to be updated.  T must extend BaseModel.
 */
public class DeleteDataAsyncTask<T extends BaseModel> extends AsyncTask<Pair<T, OnAsyncTaskResultListener>, Void, AsyncTaskResult> {
    AsyncTaskResult result;
    OnAsyncTaskResultListener listener;
    private BaseDao asyncTaskDao;

    /**
     * @param daoClass The class describing the dao object in the second parameter.
     * @param dao      The dao object containing the delete method.  The dao object must extend BaseDao.
     */
    public DeleteDataAsyncTask(Class<? extends BaseDao> daoClass, BaseDao dao) {
        asyncTaskDao = daoClass.cast(dao);
    }

    @Override
    protected AsyncTaskResult doInBackground(final Pair<T, OnAsyncTaskResultListener>... params) {
        try {
            T object = params[0].first;
            listener = params[0].second;
            asyncTaskDao.delete(object);
            result = new AsyncTaskResult(AsyncTaskResult.Result.SUCCESS);
        } catch (SQLiteConstraintException e) {
            EntityConstraintException constraintException = new EntityConstraintException(e);
            result = new AsyncTaskResult(AsyncTaskResult.Result.FAIL, constraintException);
        }
        return result;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult taskResult) {
        listener.onAsyncDeleteDataCompleted(result);
    }
}
