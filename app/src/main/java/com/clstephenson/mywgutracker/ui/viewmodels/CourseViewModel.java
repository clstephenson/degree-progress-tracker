package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseViewModel extends AndroidViewModel {

    CourseRepository courseRepository;
    TermRepository termRepository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        termRepository = new TermRepository(application);
    }

    public void getAllTermsAsList() {
        termRepository.getAllAsList();
    }

    public LiveData<Course> getCourseById(long id) {
        return courseRepository.getById(id);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    public void setDataTaskResultListener(OnDataTaskResultListener listener) {
        courseRepository.setOnDataTaskResultListener(listener);
        termRepository.setOnDataTaskResultListener(listener);
    }


}
