package com.clstephenson.mywgutracker.data;

import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.Date;

public class TestDataGenerator {

    public static Mentor createMentor() {
        return new Mentor("John", "Smith", "555-555-5555",
                "john.smith@example.com");
    }

    public static Term createTerm() {
        long todayPlus180Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 180;
        return new Term("Test Term", new Date(), new Date(todayPlus180Days));
    }

    public static Course createCourse(long mentorId, long termId) {
        long todayPlus30Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 30;
        return new Course("Mobile Application Development", new Date(), new Date(todayPlus30Days), false, true,
                CourseStatus.STARTED, mentorId, termId);
    }

    public static Assessment createAssessment(long courseId) {
        long todayPlus30Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 30;
        return new Assessment("TEST_CODE", "Mobile App Project",
                new Date(todayPlus30Days), true, courseId, AssessmentType.PERFORMANCE);
    }

    public static Note createNote(long courseId) {
        return new Note("This is a test note", courseId);
    }
}
