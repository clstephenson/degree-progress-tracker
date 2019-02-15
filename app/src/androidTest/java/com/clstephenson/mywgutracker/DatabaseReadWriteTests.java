package com.clstephenson.mywgutracker;

import android.content.Context;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.AssessmentDao;
import com.clstephenson.mywgutracker.data.db.CourseDao;
import com.clstephenson.mywgutracker.data.db.MentorDao;
import com.clstephenson.mywgutracker.data.db.NoteDao;
import com.clstephenson.mywgutracker.data.db.TermDao;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseReadWriteTests {

    private static final String TAG = DatabaseReadWriteTests.class.getSimpleName();
    private MentorDao mentorDao;
    private TermDao termDao;
    private CourseDao courseDao;
    private NoteDao noteDao;
    private AssessmentDao assessmentDao;
    private AppDatabase db;
    private Mentor mentorWrittenToDb;
    private Term termWrittenToDb;
    private Term termReadFromDb;
    private long termId;
    private long mentorId;
    private long courseId;
    private long noteId;
    private Long assessmentId;


    @Before
    public void prepareDbForTesting() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mentorDao = db.mentorDao();
        termDao = db.termDao();
        courseDao = db.courseDao();
        noteDao = db.noteDao();
        assessmentDao = db.assessmentDao();

        insertTestRecordToDbTables();
        readTestRecordFromDbTables();
    }

    private void readTestRecordFromDbTables() {
        termReadFromDb = termDao.getTermById(termId);
        Course course = courseDao.getCourseById(courseId);
        Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
        Note note = noteDao.getNoteById(noteId);
        course.setAssessments(new ArrayList<>(Collections.singletonList(assessment)));
        course.setNotes(new ArrayList<>(Collections.singletonList(note)));
        termReadFromDb.setCourses(new ArrayList<>(Collections.singletonList(course)));
    }

    private void insertTestRecordToDbTables() {
        termWrittenToDb = TestDataGenerator.createTerm();
        termId = termDao.insert(termWrittenToDb);
        termWrittenToDb.setId(termId);

        mentorWrittenToDb = TestDataGenerator.createMentor();
        mentorId = mentorDao.insert(mentorWrittenToDb);
        mentorWrittenToDb.setId(mentorId);

        Course course = TestDataGenerator.createCourse(mentorId, termId);
        courseId = courseDao.insert(course);
        course.setId(courseId);

        Note note = TestDataGenerator.createNote(courseId);
        noteId = noteDao.insert(note);
        note.setId(noteId);

        Assessment assessment = TestDataGenerator.createAssessment(courseId);
        assessmentId = assessmentDao.insert(assessment);
        assessment.setId(assessmentId);

        List<Note> notes = new ArrayList<>(Collections.singletonList(note));
        course.setNotes(notes);

        List<Assessment> assessments = new ArrayList<>(Collections.singletonList(assessment));
        course.setAssessments(assessments);

        List<Course> courses = new ArrayList<>(Collections.singletonList(course));
        termWrittenToDb.setCourses(courses);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void readMentorFromDbAndCompareWithInserted() {
        assertEquals(mentorDao.getMentorById(mentorId), mentorWrittenToDb);
    }

    @Test
    public void readNoteFromDbAndCompareWithInserted() {
        Note note = termWrittenToDb.getCourses().get(0).getNotes().get(0);
        assertEquals(noteDao.getNoteById(note.getId()), note);
    }

    @Test
    public void readAssessmentFromDbAndCompareWithInserted() {
        Assessment assessment = termWrittenToDb.getCourses().get(0).getAssessments().get(0);
        assertEquals(assessmentDao.getAssessmentById(assessmentId), assessment);
    }

    @Test
    public void readTermFromDbAndCompareWithInserted() {
        assertEquals(termReadFromDb, termWrittenToDb);
    }

}
