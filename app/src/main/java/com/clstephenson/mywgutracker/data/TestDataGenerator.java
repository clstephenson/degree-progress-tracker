package com.clstephenson.mywgutracker.data;

import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataGenerator {

    public static Mentor createMentor() {
        return new Mentor("John", "Smith", "555-555-5555",
                "john.smith@example.com");
    }

    public static Term createTermWithCourse() {
        Term term = new Term("Term 1", new Date(), new Date());
        List<Course> courses = new ArrayList<>();
        courses.add(createCourseWithNote());
        term.setCourses(courses);
        return term;
    }

    public static Course createCourseWithNote() {
        Course course = new Course("Mobile Application Development", new Date(), new Date(), false, false,
                CourseStatus.STARTED, 0, 0);
        List<Note> notes = new ArrayList<>();
        notes.add(createNote());
        course.setNotes(notes);
        return course;
    }

    public static Note createNote() {
        return new Note("This is a test note", 0);
    }
}
