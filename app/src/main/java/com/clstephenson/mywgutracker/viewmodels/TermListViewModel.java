package com.clstephenson.mywgutracker.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.data.repositories.Repository;
import com.clstephenson.mywgutracker.data.repositories.TermRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TermListViewModel extends AndroidViewModel {

    private Repository<Term> repository;
    private LiveData<List<Term>> allTerms;

    public TermListViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAll();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public void insert(Term term) {
        repository.insert(term);
    }
}
