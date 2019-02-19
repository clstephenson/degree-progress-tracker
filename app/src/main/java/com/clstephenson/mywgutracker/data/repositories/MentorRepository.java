package com.clstephenson.mywgutracker.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.clstephenson.mywgutracker.data.db.AppDatabase;
import com.clstephenson.mywgutracker.data.db.MentorDao;
import com.clstephenson.mywgutracker.data.models.Mentor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class MentorRepository implements Repository<Mentor> {

    private MentorDao mentorDao;
    private LiveData<List<Mentor>> allMentors;

    public MentorRepository(@NonNull Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mentorDao = db.mentorDao();
        allMentors = mentorDao.getAll();
    }

    public LiveData<List<Mentor>> getAll() {
        return allMentors;
    }

    @Override
    public Mentor getById(long id) {
        return mentorDao.getById(id);
    }

    @Override
    public void update(Mentor mentor) {
        mentorDao.update(mentor);
    }

    @Override
    public void deleteAll() {
        mentorDao.deleteAll();
    }

    @Override
    public void delete(Mentor mentor) {
        mentorDao.delete(mentor);
    }

    @Override
    public void deleteById(long id) {
        mentorDao.deleteById(id);
    }

    public void insert(Mentor mentor) {
        new insertAsyncTask(mentorDao).execute(mentor);
    }

    private static class insertAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao asyncTaskDao;

        insertAsyncTask(MentorDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mentor... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
