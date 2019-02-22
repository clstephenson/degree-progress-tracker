package com.clstephenson.mywgutracker.data.db;

import com.clstephenson.mywgutracker.data.models.Course;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class CourseDao implements BaseDao<Course> {

    @Query("SELECT * FROM course")
    public abstract LiveData<List<Course>> getAll();

    @Query("SELECT * FROM course WHERE term_id = :termId")
    public abstract LiveData<List<Course>> getByTermId(long termId);

    @Query("SELECT * FROM course WHERE id = :courseId")
    public abstract LiveData<Course> getById(long courseId);

    @Query("DELETE FROM course")
    public abstract void deleteAll();

    @Query("DELETE FROM course WHERE id = :id")
    public abstract void deleteById(long id);

}
