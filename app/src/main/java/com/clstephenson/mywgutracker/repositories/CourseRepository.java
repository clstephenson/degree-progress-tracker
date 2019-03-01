package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.util.Pair;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.CourseDao;
import com.clstephenson.mywgutracker.data.models.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CourseRepository implements Repository<Course> {

    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;
    private OnAsyncTaskResultListener onAsyncTaskResultListener;

    public CourseRepository(@NonNull Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAll();
    }

    @Override
    public LiveData<List<Course>> getAll() {
        return allCourses;
    }

    @Override
    public LiveData<Course> getById(long id) {
        return courseDao.getById(id);
    }

    public LiveData<List<Course>> getByTermId(long termId) {
        return courseDao.getByTermId(termId);
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Course course) {
        new DeleteDataAsyncTask<Course>(courseDao.getClass(), courseDao).execute(Pair.create(course, onAsyncTaskResultListener));
    }

    @Override
    public void update(Course course) {
        new UpdateDataAsyncTask<Course>(courseDao.getClass(), courseDao).execute(Pair.create(course, onAsyncTaskResultListener));
    }

    @Override
    public void insert(Course course) {
        new InsertDataAsyncTask<Course>(courseDao.getClass(), courseDao).execute(Pair.create(course, onAsyncTaskResultListener));
    }

    public void setOnAsyncTaskResultListener(OnAsyncTaskResultListener listener) {
        this.onAsyncTaskResultListener = listener;
    }

}
