package com.clstephenson.mywgutracker;

import android.content.Context;

import com.clstephenson.mywgutracker.data.AppDatabase;
import com.clstephenson.mywgutracker.data.TestDataGenerator;
import com.clstephenson.mywgutracker.data.dao.CourseDao;
import com.clstephenson.mywgutracker.data.dao.MentorDao;
import com.clstephenson.mywgutracker.data.dao.TermDao;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Term;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

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

    private MentorDao mentorDao;
    private TermDao termDao;
    private CourseDao courseDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mentorDao = db.mentorDao();
        termDao = db.termDao();
        courseDao = db.courseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertMentorAndReadFirstName() {
        Mentor mentorBeforeInsert = TestDataGenerator.createMentor();
        long id = mentorDao.insert(mentorBeforeInsert);
        Mentor mentorAfterInsert = mentorDao.getMentorById(id);
        assertEquals(mentorBeforeInsert.getFirstName(), mentorAfterInsert.getFirstName());
    }

    @Test
    public void insertCourseAndReadCourseName() {
        Term term = TestDataGenerator.createTermWithCourse();
        long termId = termDao.insert(term);
        Course courseBeforeInsert = term.getCourses().get(0);
        courseBeforeInsert.setTermId(termId);
        Mentor mentor = TestDataGenerator.createMentor();
        courseBeforeInsert.setMentorId(mentorDao.insert(mentor));
        long courseId = courseDao.insert(courseBeforeInsert);
        Course courseAfterInsert = courseDao.getCourseById(courseId);
        assertEquals(courseAfterInsert.getName(), courseBeforeInsert.getName());
    }
}
