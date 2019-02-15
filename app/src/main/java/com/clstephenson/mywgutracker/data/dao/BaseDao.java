package com.clstephenson.mywgutracker.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    public abstract long insert(T t);

    @Insert
    public abstract long[] insert(T... t);

    @Update
    public abstract void update(T... t);

    @Delete
    public abstract void delete(T... t);

}
