package com.clstephenson.mywgutracker.data.dao;

import com.clstephenson.mywgutracker.data.models.Course;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class CourseDao implements BaseDao<Course> {

    @Query("SELECT * FROM course")
    public abstract LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course WHERE term_id = :termId")
    public abstract LiveData<List<Course>> getCoursesByTermId(long termId);

    @Query("SELECT * FROM course WHERE id = :courseId")
    public abstract Course getCourseById(long courseId);

}
