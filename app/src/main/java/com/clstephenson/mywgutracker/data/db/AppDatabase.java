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

//    private static AppDatabase.Callback appDatabaseCallback =
//            new AppDatabase.Callback() {
//                @Override
//                public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                    super.onOpen(db);
//                    new SeedDBAsync(INSTANCE).execute();
//                }
//            };

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
        private final TermDao termDao;
        private final CourseDao courseDao;
        private final AssessmentDao assessmentDao;
        private final MentorDao mentorDao;
        private final NoteDao noteDao;

        ClearDataAsync(AppDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
            mentorDao = db.mentorDao();
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAll();
            courseDao.deleteAll();
            assessmentDao.deleteAll();
            mentorDao.deleteAll();
            noteDao.deleteAll();
            return null;
        }
    }

    private static class SeedDBAsync extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;

        SeedDBAsync(AppDatabase db) {
            termDao = db.termDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Term term = TestDataGenerator.createTerm();
            termDao.insert(term);
            return null;
        }
    }

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract MentorDao mentorDao();

    public abstract NoteDao noteDao();

    public abstract TermDao termDao();


}
