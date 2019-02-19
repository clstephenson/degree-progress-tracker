package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Assessment;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class AssessmentDao implements BaseDao<Assessment> {

    @Query("SELECT * FROM assessment")
    public abstract LiveData<List<Assessment>> getAll();

    @Query("SELECT * FROM assessment WHERE course_id = :courseId")
    public abstract LiveData<List<Assessment>> getByCourseId(long courseId);

    @Query("SELECT * FROM assessment WHERE id = :assessmentId")
    public abstract Assessment getById(long assessmentId);

    @Query("DELETE FROM assessment")
    public abstract void deleteAll();

    @Query("DELETE FROM assessment WHERE id = :id")
    public abstract void deleteById(long id);

}
