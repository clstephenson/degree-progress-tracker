package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Mentor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class MentorDao implements BaseDao<Mentor> {

    @Query("SELECT * FROM mentor ORDER BY last_name ASC")
    public abstract LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM mentor WHERE id = :mentorId")
    public abstract Mentor getMentorById(long mentorId);

}