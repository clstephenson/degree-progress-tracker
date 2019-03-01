package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.util.Pair;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.AssessmentDao;
import com.clstephenson.mywgutracker.data.models.Assessment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class AssessmentRepository implements Repository<Assessment> {

    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessments;
    private OnAsyncTaskResultListener onAsyncTaskResultListener;

    public AssessmentRepository(@NonNull Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        assessmentDao = db.assessmentDao();
        allAssessments = assessmentDao.getAll();
    }

    @Override
    public LiveData<List<Assessment>> getAll() {
        return assessmentDao.getAll();
    }

    @Override
    public LiveData<Assessment> getById(long id) {
        return assessmentDao.getById(id);
    }

    public LiveData<List<Assessment>> getByCourseId(long id) {
        return assessmentDao.getByCourseId(id);
    }

    @Override
    public void insert(Assessment assessment) {
        new InsertDataAsyncTask<Assessment>(AssessmentDao.class, assessmentDao).execute(Pair.create(assessment, onAsyncTaskResultListener));
    }

    @Override
    public void update(Assessment assessment) {
        new UpdateDataAsyncTask<Assessment>(AssessmentDao.class, assessmentDao).execute(Pair.create(assessment, onAsyncTaskResultListener));
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Assessment assessment) {
        new DeleteDataAsyncTask<Assessment>(AssessmentDao.class, assessmentDao).execute(Pair.create(assessment, onAsyncTaskResultListener));
    }

    public void setOnAsyncTaskResultListener(OnAsyncTaskResultListener listener) {
        this.onAsyncTaskResultListener = listener;
    }

}
