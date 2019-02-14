package com.clstephenson.mywgutracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentor")
public class Mentor {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "first_name")
    private String mFirstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    private String mLastName;

    @ColumnInfo(name = "phone")
    private String mPhone;

    @ColumnInfo(name = "email")
    private String mEmail;

    public Mentor(int mId, @NonNull String firstName, @NonNull String lastName, String phone, String email) {
        this.mId = mId;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mPhone = phone;
        this.mEmail = email;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getFirstName() {
        return mFirstName;
    }

    @NonNull
    public String getLastName() {
        return mLastName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEmail() {
        return mEmail;
    }
}
