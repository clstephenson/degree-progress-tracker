package com.clstephenson.mywgutracker;

import com.clstephenson.mywgutracker.data.AssessmentType;
import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.Date;

public class TestDataGenerator {

    public static Mentor createMentor() {
        return new Mentor("John", "Smith", "555-555-5555",
                "john.smith@example.com");
    }

    public static Term createTerm() {
        return new Term("Term 1", new Date(), new Date());
    }

    public static Course createCourse(long mentorId, long termId) {
        return new Course("Mobile Application Development", new Date(), new Date(), false, false,
                CourseStatus.STARTED, mentorId, termId);
    }

    public static Assessment createAssessment(long courseId) {
        return new Assessment("TEST_CODE", "Mobile App Project",
                new Date(), false, courseId, AssessmentType.PERFORMANCE);
    }

    public static Note createNote(long courseId) {
        return new Note("This is a test note", courseId);
    }
}
