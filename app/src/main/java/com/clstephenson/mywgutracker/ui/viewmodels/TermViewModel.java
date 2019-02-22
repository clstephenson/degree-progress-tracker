package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.data.repositories.Repository;
import com.clstephenson.mywgutracker.data.repositories.TermRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TermViewModel extends AndroidViewModel {

    Repository<Term> termRepository;

    public TermViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
    }

    public LiveData<Term> getTermById(long id) {
        return termRepository.getById(id);
    }
}
