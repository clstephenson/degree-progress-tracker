package com.clstephenson.mywgutracker.repositories;

public interface OnAsyncTaskResultListener {
    void onAsyncDeleteDataCompleted(AsyncTaskResult result);

    void onAsyncUpdateDataCompleted(AsyncTaskResult result);

    void onAsyncInsertDataCompleted(AsyncTaskResult result);
}
