package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.AssessmentType;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.repositories.AssessmentRepository;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AssessmentEditViewModel extends AndroidViewModel {

    AssessmentRepository assessmentRepository;

    public AssessmentEditViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = new AssessmentRepository(application);
    }

    public Assessment getNewAssessment(long courseId) {
        return new Assessment("", "", new Date(), false, courseId,
                AssessmentType.OBJECTIVE);
    }

    public LiveData<Assessment> getAssessmentById(long id) {
        return assessmentRepository.getById(id);
    }

    public void deleteAssessment(Assessment assessment) {
        assessmentRepository.delete(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        assessmentRepository.update(assessment);
    }

    public void setDataTaskResultListener(OnDataTaskResultListener listener) {
        assessmentRepository.setOnDataTaskResultListener(listener);
    }

    public void insertAssessment(Assessment assessment) {
        assessmentRepository.insert(assessment);
    }
}
