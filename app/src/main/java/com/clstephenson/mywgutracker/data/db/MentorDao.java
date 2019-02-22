package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Mentor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class MentorDao implements BaseDao<Mentor> {

    @Query("SELECT * FROM mentor ORDER BY last_name ASC")
    public abstract LiveData<List<Mentor>> getAll();

    @Query("SELECT * FROM mentor WHERE id = :mentorId")
    public abstract LiveData<Mentor> getById(long mentorId);

    @Query("DELETE FROM mentor")
    public abstract void deleteAll();

    @Query("DELETE FROM mentor WHERE id = :id")
    public abstract void deleteById(long id);

}
