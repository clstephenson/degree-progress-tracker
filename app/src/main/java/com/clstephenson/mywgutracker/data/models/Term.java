package com.clstephenson.mywgutracker.data.models;

import com.clstephenson.mywgutracker.data.TermStatus;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "term")
public class Term extends BaseModel {

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "start_date")
    private Date startDate;

    @NonNull
    @ColumnInfo(name = "end_date")
    private Date endDate;

    @Ignore
    private List<Course> courses = Collections.emptyList();

    public Term(long id, @NonNull String name, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Term(@NonNull String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(@NonNull Date endDate) {
        this.endDate = endDate;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public TermStatus getStatus() {
        if (DateUtils.isDateBeforeToday(this.endDate)) {
            return TermStatus.COMPLETED;
        } else if (DateUtils.isDateAfterToday(this.startDate)) {
            return TermStatus.NOT_STARTED;
        } else {
            return TermStatus.IN_PROGRESS;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(name, term.name) &&
                Objects.equals(startDate, term.startDate) &&
                Objects.equals(endDate, term.endDate) &&
                Objects.equals(courses, term.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate, courses);
    }

    @Override
    public String toString() {
        return "Term{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", courses=" + courses +
                '}';
    }
}
