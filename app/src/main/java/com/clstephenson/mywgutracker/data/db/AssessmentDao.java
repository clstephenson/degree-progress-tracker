package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Assessment;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class AssessmentDao implements BaseDao<Assessment> {

    @Query("SELECT * FROM assessment")
    public abstract LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessment WHERE course_id = :courseId")
    public abstract LiveData<List<Assessment>> getAssessmentsByCourseId(long courseId);

    @Query("SELECT * FROM assessment WHERE id = :assessmentId")
    public abstract Assessment getAssessmentById(long assessmentId);

}
