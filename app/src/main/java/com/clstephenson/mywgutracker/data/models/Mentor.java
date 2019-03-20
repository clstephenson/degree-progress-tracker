package com.clstephenson.mywgutracker.data.models;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class Mentor {

    @NonNull
    @ColumnInfo(name = "mentor_first_name")
    private String firstName;

    @NonNull
    @ColumnInfo(name = "mentor_last_name")
    private String lastName;

    @ColumnInfo(name = "mentor_phone")
    private String phone;

    @ColumnInfo(name = "mentor_email")
    private String email;

    @NonNull
    @ColumnInfo(name = "mentor_2_first_name")
    private String firstName2;

    @NonNull
    @ColumnInfo(name = "mentor_2_last_name")
    private String lastName2;

    @ColumnInfo(name = "mentor_2_phone")
    private String phone2;

    @ColumnInfo(name = "mentor_2_email")
    private String email2;

    @NonNull
    @ColumnInfo(name = "mentor_3_first_name")
    private String firstName3;

    @NonNull
    @ColumnInfo(name = "mentor_3_last_name")
    private String lastName3;

    @ColumnInfo(name = "mentor_3_phone")
    private String phone3;

    @ColumnInfo(name = "mentor_3_email")
    private String email3;

    public Mentor(String firstName, String lastName, String phone, String email,
                  String firstName2, String lastName2, String phone2, String email2,
                  String firstName3, String lastName3, String phone3, String email3) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.phone = phone != null ? phone : "";
        this.email = email != null ? email : "";
        this.firstName2 = firstName2 != null ? firstName2 : "";
        this.lastName2 = lastName2 != null ? lastName2 : "";
        this.phone2 = phone2 != null ? phone2 : "";
        this.email2 = email2 != null ? email2 : "";
        this.firstName3 = firstName3 != null ? firstName3 : "";
        this.lastName3 = lastName3 != null ? lastName3 : "";
        this.phone3 = phone3 != null ? phone3 : "";
        this.email3 = email3 != null ? email3 : "";
    }

    @Ignore
    public Mentor(Mentor mentor) {
        this(mentor.getFirstName(), mentor.getLastName(), mentor.getPhone(), mentor.getEmail(),
                mentor.getFirstName2(), mentor.getLastName2(), mentor.getPhone2(), mentor.getEmail2(),
                mentor.getFirstName3(), mentor.getLastName3(), mentor.getPhone3(), mentor.getEmail3());
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getFirstName2() {
        return firstName2;
    }

    public void setFirstName2(@NonNull String firstName2) {
        this.firstName2 = firstName2;
    }

    @NonNull
    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(@NonNull String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    @NonNull
    public String getFirstName3() {
        return firstName3;
    }

    public void setFirstName3(@NonNull String firstName3) {
        this.firstName3 = firstName3;
    }

    @NonNull
    public String getLastName3() {
        return lastName3;
    }

    public void setLastName3(@NonNull String lastName3) {
        this.lastName3 = lastName3;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mentor mentor = (Mentor) o;
        return Objects.equals(firstName, mentor.firstName) &&
                Objects.equals(lastName, mentor.lastName) &&
                Objects.equals(phone, mentor.phone) &&
                Objects.equals(email, mentor.email) &&
                Objects.equals(firstName2, mentor.firstName2) &&
                Objects.equals(lastName2, mentor.lastName2) &&
                Objects.equals(phone2, mentor.phone2) &&
                Objects.equals(email2, mentor.email2) &&
                Objects.equals(firstName3, mentor.firstName3) &&
                Objects.equals(lastName3, mentor.lastName3) &&
                Objects.equals(phone3, mentor.phone3) &&
                Objects.equals(email3, mentor.email3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone, email, firstName2, lastName2, phone2, email2,
                firstName3, lastName3, phone3, email3);
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }
}
