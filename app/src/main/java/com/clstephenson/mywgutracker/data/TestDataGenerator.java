package com.clstephenson.mywgutracker.data;

import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.Date;
import java.util.Locale;

public class TestDataGenerator {

    public static Mentor createMentor() {
        return new Mentor("John", "Smith", "555-555-5555",
                "john.smith@example.com");
    }

    public static Term createTerm(int numToAppend) {
        long todayPlus180Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 180;
        return new Term(String.format(Locale.getDefault(), "Test Term %d", numToAppend), new Date(), new Date(todayPlus180Days));
    }

    public static Course createCourse(int numToAppend, long termId) {
        long todayPlus30Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 30;
        Mentor mentor = createMentor();
        return new Course(String.format(Locale.getDefault(), "Test Course %d", numToAppend), new Date(), new Date(todayPlus30Days), false, false,
                CourseStatus.STARTED, mentor, termId);
    }

    public static Assessment createAssessment(int numToAppend, long courseId) {
        long todayPlus30Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 30;
        return new Assessment(String.format(Locale.getDefault(), "Test Code %d", numToAppend), String.format(Locale.getDefault(), "Test Assessment %d", numToAppend),
                new Date(todayPlus30Days), false, courseId, AssessmentType.PERFORMANCE);
    }

    public static Note createNote(int numToAppend, long courseId) {
        return new Note(String.format(Locale.getDefault(), "%d - %s", numToAppend,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
                courseId);
    }
}
