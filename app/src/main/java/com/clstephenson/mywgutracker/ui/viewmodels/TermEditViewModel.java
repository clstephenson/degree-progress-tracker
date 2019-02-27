package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.OnAsyncTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TermEditViewModel extends AndroidViewModel {

    TermRepository termRepository;

    public TermEditViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
    }

    public LiveData<Term> getTermById(long id) {
        return termRepository.getById(id);
    }

    public void deleteTerm(Term term) {
        termRepository.delete(term);
    }

    public void updateTerm(Term term) {
        termRepository.update(term);
    }

    public void setBackgroundTaskResultListener(OnAsyncTaskResultListener delegate) {
        termRepository.setOnAsyncTaskResultListener(delegate);
    }

}
