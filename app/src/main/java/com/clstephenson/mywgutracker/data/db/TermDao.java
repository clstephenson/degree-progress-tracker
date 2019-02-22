package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class TermDao implements BaseDao<Term> {

    @Query("SELECT * FROM term")
    public abstract LiveData<List<Term>> getAll();

    @Query("SELECT * FROM term WHERE id = :id")
    public abstract LiveData<Term> getById(long id);

    @Query("DELETE FROM term")
    public abstract void deleteAll();

    @Query("DELETE FROM term WHERE id = :id")
    public abstract void deleteById(long id);

}
