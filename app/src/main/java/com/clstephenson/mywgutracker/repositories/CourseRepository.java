package com.clstephenson.mywgutracker.repositories;

import android.app.Application;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.CourseDao;
import com.clstephenson.mywgutracker.data.models.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CourseRepository implements Repository<Course> {

    private final CourseDao courseDao;
    private final LiveData<List<Course>> allCourses;
    private OnDataTaskResultListener onDataTaskResultListener;

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
        new DataTask<Course>(courseDao.getClass(), courseDao, DataTask.Action.DELETE, onDataTaskResultListener).execute(course);
    }

    @Override
    public void update(Course course) {
        new DataTask<Course>(courseDao.getClass(), courseDao, DataTask.Action.UPDATE, onDataTaskResultListener).execute(course);
    }

    @Override
    public void insert(Course course) {
        new DataTask<Course>(courseDao.getClass(), courseDao, DataTask.Action.INSERT, onDataTaskResultListener).execute(course);
    }

    public void setOnDataTaskResultListener(OnDataTaskResultListener listener) {
        this.onDataTaskResultListener = listener;
    }

}
