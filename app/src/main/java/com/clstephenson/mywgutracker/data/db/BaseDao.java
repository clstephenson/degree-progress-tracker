package com.clstephenson.mywgutracker.data.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    long insert(T object);

    @Insert
    long[] insert(T... objects);

    @Update
    void update(T... objects);

    @Update
    void update(T object);

    @Delete
    void delete(T... objects);

    @Delete
    void delete(T object);

}
