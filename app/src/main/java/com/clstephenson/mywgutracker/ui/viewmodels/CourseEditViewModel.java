package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseEditViewModel extends AndroidViewModel {

    private final CourseRepository courseRepository;
    private final TermRepository termRepository;

    public CourseEditViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        termRepository = new TermRepository(application);
    }

    public void getAllTermsAsList() {
        termRepository.getAllAsList();
    }

    public Course getNewCourse(long termId) {
        return new Course("", new Date(), new Date(), false, false,
                CourseStatus.NOT_STARTED, createNewMentor(), termId);
    }

    public LiveData<Course> getCourseById(long id) {
        return courseRepository.getById(id);
    }

    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    public void setDataTaskResultListener(OnDataTaskResultListener listener) {
        courseRepository.setOnDataTaskResultListener(listener);
        termRepository.setOnDataTaskResultListener(listener);
    }

    public void insertCourse(Course course) {
        courseRepository.insert(course);
    }

    public Mentor createNewMentor() {
        return new Mentor("", "", "", "", "", "",
                "", "", "", "", "", "");
    }
}
