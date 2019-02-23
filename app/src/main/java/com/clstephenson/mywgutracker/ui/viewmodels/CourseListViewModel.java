package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.repositories.CourseRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseListViewModel extends AndroidViewModel {

    private CourseRepository repository;
    private LiveData<List<Course>> allCourses;

    public CourseListViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getAll();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Course>> getCoursesByTermId(long termId) {
        return repository.getByTermId(termId);
    }

    public void insert(Course course) {
        repository.insert(course);
    }
}
