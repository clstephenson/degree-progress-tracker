package com.clstephenson.mywgutracker.repositories;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.clstephenson.mywgutracker.data.db.BaseDao;
import com.clstephenson.mywgutracker.data.models.BaseModel;


/**
 * @param <T> The entity model class to be updated.  T must extend BaseModel.
 */
public class DataTask<T extends BaseModel> extends AsyncTask<T, Void, DataTaskResult> {

    private OnDataTaskResultListener listener;
    private BaseDao asyncTaskDao;
    private Action action;
    private final String TAG = this.getClass().getSimpleName();

    /**
     * @param daoClass The class describing the dao object in the second parameter.
     * @param dao      The dao object containing the update method.  The dao object must extend BaseDao.
     */
    public DataTask(Class<? extends BaseDao> daoClass, BaseDao dao, Action action, OnDataTaskResultListener listener) {
        this.action = action;
        asyncTaskDao = daoClass.cast(dao);
        this.listener = listener;
    }

    @Override
    protected DataTaskResult doInBackground(final T... params) {
        DataTaskResult dataTaskResult = new DataTaskResult(action, DataTaskResult.Result.FAIL);
        try {
            T object = params[0];
            switch (action) {
                case DELETE:
                    asyncTaskDao.delete(object);
                    break;
                case INSERT:
                    long insertId = asyncTaskDao.insert(object);
                    dataTaskResult.setData(insertId);
                    break;
                case UPDATE:
                    asyncTaskDao.update(object);
                    break;
                case GET:
                    //todo: implement this
                    throw new UnsupportedOperationException();
                    //break;
            }
            dataTaskResult.setResult(DataTaskResult.Result.SUCCESS);
            return dataTaskResult;
        } catch (SQLiteConstraintException e) {
            Log.d(TAG, "doInBackground: " + e.getLocalizedMessage());
            dataTaskResult.setConstraintException(new EntityConstraintException(e));
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.getLocalizedMessage());
            dataTaskResult.setOtherException(e);
        } finally {
            return dataTaskResult;
        }
    }

    @Override
    protected void onPostExecute(DataTaskResult taskResult) {
        listener.onNotifyDataChanged(taskResult);
    }

    public enum Action {
        INSERT, UPDATE, DELETE, GET
    }
}
