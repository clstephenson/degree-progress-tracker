package com.clstephenson.mywgutracker.data.db;

import android.content.Context;

import com.clstephenson.mywgutracker.data.DataTypeConverter;
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

    static AppDatabase getDatabase(final Context context) {
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

    public abstract AssessmentDao assessmentDao();

    public abstract CourseDao courseDao();

    public abstract MentorDao mentorDao();

    public abstract NoteDao noteDao();

    public abstract TermDao termDao();
}
