package com.clstephenson.mywgutracker.ui.viewmodels;

import android.app.Application;

import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.repositories.Repository;
import com.clstephenson.mywgutracker.repositories.TermRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TermListViewModel extends AndroidViewModel {

    private final LiveData<List<Term>> allTerms;

    public TermListViewModel(@NonNull Application application) {
        super(application);
        Repository<Term> repository = new TermRepository(application);
        allTerms = repository.getAll();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public Term getTerm(int position) {
        return allTerms.getValue().get(position);
    }
}
