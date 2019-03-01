package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.repositories.AssessmentRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AssessmentListViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentListViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAll();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourseId(long courseId) {
        return repository.getByCourseId(courseId);
    }

    public Assessment getAssessment(int position) {
        return allAssessments.getValue().get(position);
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }
}
