package com.clstephenson.mywgutracker.data.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseModel {

    @SuppressWarnings("WeakerAccess")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
