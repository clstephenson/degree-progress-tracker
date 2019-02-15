package com.clstephenson.mywgutracker.data.dao;

import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class TermDao implements BaseDao<Term> {

    @Query("SELECT * FROM term")
    public abstract LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM term WHERE id = :termId")
    public abstract Term getTermById(long termId);

}
