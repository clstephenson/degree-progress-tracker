package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.OnDataTaskResultListener;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TermEditViewModel extends AndroidViewModel {

    private final TermRepository termRepository;

    public TermEditViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
    }

    public Term getNewTerm() {
        return new Term("", new Date(), new Date());
    }

    public LiveData<Term> getTermById(long id) {
        return termRepository.getById(id);
    }

    public void updateTerm(Term term) {
        termRepository.update(term);
    }

    public void setBackgroundTaskResultListener(OnDataTaskResultListener listener) {
        termRepository.setOnDataTaskResultListener(listener);
    }

    public void insertTerm(Term term) {
        termRepository.insert(term);
    }
}
