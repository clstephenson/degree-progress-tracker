package com.clstephenson.mywgutracker.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.CourseDao;
import com.clstephenson.mywgutracker.data.models.Course;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CourseRepository implements Repository<Course> {

    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

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
        courseDao.deleteById(id);
    }

    @Override
    public void delete(Course object) {
        courseDao.delete(object);
    }

    @Override
    public void update(Course course) {
        courseDao.update(course);
    }

    @Override
    public void insert(Course course) {
        new CourseRepository.insertAsyncTask(courseDao).execute(course);
    }

    private static class insertAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao asyncTaskDao;

        insertAsyncTask(CourseDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Course... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
