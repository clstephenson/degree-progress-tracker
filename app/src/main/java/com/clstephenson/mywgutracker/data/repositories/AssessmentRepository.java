package com.clstephenson.mywgutracker.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.AssessmentDao;
import com.clstephenson.mywgutracker.data.models.Assessment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class AssessmentRepository implements Repository<Assessment> {

    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessments;

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
    public Assessment getById(long id) {
        return assessmentDao.getById(id);
    }

    @Override
    public void insert(Assessment assessment) {
        new AssessmentRepository.insertAsyncTask(assessmentDao).execute(assessment);
    }

    @Override
    public void update(Assessment assessment) {
        assessmentDao.update(assessment);
    }

    @Override
    public void deleteAll() {
        assessmentDao.deleteAll();
    }

    @Override
    public void delete(Assessment assessment) {
        assessmentDao.delete(assessment);
    }

    @Override
    public void deleteById(long id) {
        assessmentDao.deleteById(id);
    }

    private static class insertAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao asyncTaskDao;

        insertAsyncTask(AssessmentDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
