package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseEditViewModel extends AndroidViewModel {

    CourseRepository courseRepository;
    TermRepository termRepository;
    List<Term> allTerms;

    public CourseEditViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        termRepository = new TermRepository(application);
        termRepository.getAll().observeForever(this::setAllTerms);
    }

    public List<Term> getAllTerms() {
        return allTerms;
    }

    private void setAllTerms(List<Term> terms) {
        allTerms = terms;
    }

    @Nullable
    public Term getTermById(long id) {
        for (Term term : allTerms) {
            if (term.getId() == id) {
                return term;
            }
        }
        return null;
    }

    public Course getNewCourse() {
        return new Course("", new Date(), new Date(), false, false,
                CourseStatus.NOT_STARTED, new Mentor("", "", "", ""), 0);
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
