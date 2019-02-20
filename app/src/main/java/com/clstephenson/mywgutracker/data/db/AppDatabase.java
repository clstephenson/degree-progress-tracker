package com.clstephenson.mywgutracker.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.DataTypeConverter;
import com.clstephenson.mywgutracker.data.TestDataGenerator;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Mentor;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Assessment.class, Course.class, Mentor.class, Note.class, Term.class}, version = 1)
@TypeConverters({DataTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "my_wgu_tracker_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void clearData() {
        new ClearDataAsync(INSTANCE).execute();
    }

    public static void seedDatabase() {
        new SeedDBAsync(INSTANCE).execute();
    }

    private static class ClearDataAsync extends AsyncTask<Void, Void, Void> {

        ClearDataAsync(AppDatabase db) {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            INSTANCE.clearAllTables();
            return null;
        }
    }

    private static class SeedDBAsync extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;
        private final MentorDao mentorDao;
        private final CourseDao courseDao;

        SeedDBAsync(AppDatabase db) {
            termDao = db.termDao();
            mentorDao = db.mentorDao();
            courseDao = db.courseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long mentorId = mentorDao.insert(TestDataGenerator.createMentor());
            Term term = TestDataGenerator.createTerm();
            long termId = termDao.insert(term);
            Course course = TestDataGenerator.createCourse(mentorId, termId);
            long courseId = courseDao.insert(course);
            return null;
        }
    }

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract MentorDao mentorDao();

    public abstract NoteDao noteDao();

    public abstract TermDao termDao();


}
