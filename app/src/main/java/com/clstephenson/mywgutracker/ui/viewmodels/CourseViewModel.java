package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.CourseRepository;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CourseViewModel extends AndroidViewModel {

    CourseRepository courseRepository;
    TermRepository termRepository;
    List<Term> allTerms;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        termRepository = new TermRepository(application);
        termRepository.getAll().observeForever(this::setAllTerms);
    }

    private void setAllTerms(List<Term> terms) {
        allTerms = terms;
    }

    public Term getTerm(Course course) {
        for (Term term : allTerms) {
            if (term.getId() == course.getTermId()) {
                return term;
            }
        }
        return null;
    }

    public LiveData<Course> getCourseById(long id) {
        return courseRepository.getById(id);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    public void setDataTaskResultListener(OnDataTaskResultListener listener) {
        courseRepository.setOnDataTaskResultListener(listener);
    }


}
