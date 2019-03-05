package com.clstephenson.mywgutracker.data.db;

import android.content.Context;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.DataTypeConverter;
import com.clstephenson.mywgutracker.data.TestDataGenerator;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.data.models.Note;
import com.clstephenson.mywgutracker.data.models.Term;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Assessment.class, Course.class, Note.class, Term.class}, version = 1)
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

        AppDatabase db;

        ClearDataAsync(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.clearAllTables();
            return null;
        }
    }

    private static class SeedDBAsync extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;
        private final CourseDao courseDao;
        private final AssessmentDao assessmentDao;
        private final NoteDao noteDao;

        SeedDBAsync(AppDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i < 3; i++) {
                Term term = TestDataGenerator.createTerm(i);
                long termId = termDao.insert(term);
                for (int j = 1; j < 3; j++) {
                    Course course = TestDataGenerator.createCourse(j, termId);
                    long courseId = courseDao.insert(course);
                    for (int k = 1; k < 3; k++) {
                        Assessment assessment = TestDataGenerator.createAssessment(k, courseId);
                        assessmentDao.insert(assessment);
                        Note note = TestDataGenerator.createNote(k, courseId);
                        noteDao.insert(note);
                    }
                }
            }
            return null;
        }
    }

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract NoteDao noteDao();

    public abstract TermDao termDao();


}
