package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseEditViewModel extends AndroidViewModel {

    CourseRepository courseRepository;

    public CourseEditViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
    }

    public Course getNewCourse() {
        return new Course("", new Date(), new Date(), false, false,
                CourseStatus.NOT_STARTED, 0, 0);
    }

    public LiveData<Course> getCourseById(long id) {
        return courseRepository.getById(id);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    public void setBackgroundTaskResultListener(OnAsyncTaskResultListener delegate) {
        courseRepository.setOnAsyncTaskResultListener(delegate);
    }

    public void insertCourse(Course course) {
        courseRepository.insert(course);
    }
}
