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

    public static final int MAX_TEST_TERMS = 3;
    public static final int MAX_TEST_COURSES = 3;
    private static Mentor[] mentorTestData;
    private static Term[] termTestData;
    private static Course[] courseTestData;
    private static int numMentors;

    static {
        mentorTestData = new Mentor[]{
                new Mentor("John", "Smith", "555-555-5555", "john.smith@example.com"),
                new Mentor("Mike", "Jones", "444-444-4444", "mike.jones@example.com"),
                new Mentor("Chris", "Johnson", "333-333-3333", "chris.johnson@example.com")
        };
        termTestData = new Term[]{
                new Term(String.format(Locale.getDefault(), "Term %d", 1), DateUtils.getDate(2018, 5, 1), DateUtils.getDate(2018, 10, 30)),
                new Term(String.format(Locale.getDefault(), "Term %d", 2), DateUtils.getDate(2018, 11, 1), DateUtils.getDate(2019, 4, 31)),
                new Term(String.format(Locale.getDefault(), "Term %d", 3), DateUtils.getDate(2019, 5, 1), DateUtils.getDate(2019, 10, 30))
        };
        courseTestData = new Course[]{
                new Course("C179 Business of IT - Applications", DateUtils.getDate(2018, 5, 1), DateUtils.getDate(2018, 5, 30), false, false, CourseStatus.COMPLETED, createMentor(), 0),
                new Course("C483 Principles of Management", DateUtils.getDate(2018, 6, 1), DateUtils.getDate(2018, 6, 31), false, false, CourseStatus.COMPLETED, createMentor(), 0),
                new Course("C189 Data Structures", DateUtils.getDate(2018, 7, 1), DateUtils.getDate(2018, 7, 31), false, false, CourseStatus.COMPLETED, createMentor(), 0),
                new Course("C188 Software Engineering", DateUtils.getDate(2018, 11, 1), DateUtils.getDate(2019, 1, 28), false, false, CourseStatus.COMPLETED, createMentor(), 0),
                new Course("C196 Mobile Application Development", DateUtils.getDate(2019, 2, 1), DateUtils.getDate(2019, 2, 31), false, false, CourseStatus.STARTED, createMentor(), 0),
                new Course("C193 Client-Server Application Development", DateUtils.getDate(2019, 3, 1), DateUtils.getDate(2019, 4, 31), false, false, CourseStatus.NOT_STARTED, createMentor(), 0),
                new Course("C482 Software 1", DateUtils.getDate(2019, 5, 1), DateUtils.getDate(2019, 5, 30), false, false, CourseStatus.NOT_STARTED, createMentor(), 0),
                new Course("C195 Software 2", DateUtils.getDate(2019, 6, 1), DateUtils.getDate(2019, 6, 31), false, false, CourseStatus.NOT_STARTED, createMentor(), 0),
                new Course("C769 IT Capstone Written Project", DateUtils.getDate(2019, 1, 1), DateUtils.getDate(2019, 10, 30), false, false, CourseStatus.NOT_STARTED, createMentor(), 0),
        };
    }

    public static Mentor createMentor() {
        numMentors++;
        int index;
        if (numMentors % 3 == 0) {
            index = 2;
        } else if (numMentors % 2 == 0) {
            index = 1;
        } else {
            index = 0;
        }
        if (numMentors == 3) numMentors = 0;
        return mentorTestData[index];
    }

    public static Term createTerm(int termIndex) {
        return termTestData[termIndex];
    }

    public static Course createCourse(int termIndex, int courseIndex, long termId) {
        int startingCourseIndex = termIndex * 3;
        Course course = courseTestData[startingCourseIndex + courseIndex];
        course.setTermId(termId);
        return course;
    }

    public static Assessment createAssessment(int numToAppend, long courseId) {
        long todayPlus30Days = new Date().getTime() + DateUtils.MILLISECONDS_IN_DAY * 30;
        return new Assessment(String.format(Locale.getDefault(), "Test Assessment %d", numToAppend),
                new Date(todayPlus30Days), false, courseId, AssessmentType.PERFORMANCE);
    }

    public static Note createNote(int numToAppend, long courseId) {
        return new Note(String.format(Locale.getDefault(), "%d - %s", numToAppend,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
                courseId);
    }
}
