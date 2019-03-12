package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.repositories.AssessmentRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AssessmentListViewModel extends AndroidViewModel {

    private final AssessmentRepository repository;
    private LiveData<List<Assessment>> assessments;

    public AssessmentListViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        assessments = repository.getAll();
    }

    public LiveData<List<Assessment>> getAssessments() {
        return assessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourseId(long courseId) {
        assessments = repository.getByCourseId(courseId);
        return assessments;
    }

    public Assessment getAssessment(int position) {
        return assessments.getValue().get(position);
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }
}
