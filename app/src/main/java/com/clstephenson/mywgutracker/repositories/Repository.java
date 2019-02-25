package com.clstephenson.mywgutracker.repositories;

import com.clstephenson.mywgutracker.data.models.BaseModel;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface Repository<T extends BaseModel> {
    LiveData<List<T>> getAll();

    LiveData<T> getById(long id);

    void insert(T object);

    void update(T object);

    void deleteById(long id);
}
