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
    private LiveData<List<Course>> courses;

    public CourseListViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        courses = repository.getAll();
    }

    public LiveData<List<Course>> getCourses() {
        return courses;
    }

    public LiveData<List<Course>> getCoursesByTermId(long termId) {
        courses = repository.getByTermId(termId);
        return courses;
    }

    public Course getCourse(int position) {
        return courses.getValue().get(position);
    }

    public void insert(Course course) {
        repository.insert(course);
    }
}
