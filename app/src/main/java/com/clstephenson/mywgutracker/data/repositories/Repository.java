package com.clstephenson.mywgutracker.data.repositories;

import com.clstephenson.mywgutracker.data.models.BaseModel;

import java.util.List;

import androidx.lifecycle.LiveData;

interface Repository<T extends BaseModel> {
    LiveData<List<T>> getAll();

    T getById(long id);

    void insert(T object);

    void update(T object);

    void deleteAll();

    void delete(T object);

    void deleteById(long id);
}
