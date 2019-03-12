package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.Repository;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Term>> allTerms;
    private final LiveData<List<Course>> allCourses;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        Repository<Term> termRepository = new TermRepository(application);
        Repository<Course> courseRepository = new CourseRepository(application);
        allTerms = termRepository.getAll();
        allCourses = courseRepository.getAll();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
}
