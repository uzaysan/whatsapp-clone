package com.uzaysan.whatsappclone.models.user;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.uzaysan.whatsappclone.database.AppDatabase;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatDao;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private LiveData<User> userLiveData;
    private UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        userDao = appDatabase.userDao();
    }

    public LiveData<User> getUserLiveData(String id) {
        userLiveData = userDao.getUserById(id);
        return userLiveData;
    }

    public void insertUser(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        new InsertUserAsyncTask(userDao).execute(users);
    }

    public void insertUsers(List<User> users) {
        new InsertUserAsyncTask(userDao).execute(users);
    }

    public void deleteAll() {
        new InsertUserAsyncTask(userDao).execute();
    }

    private static class InsertUserAsyncTask extends AsyncTask<List<User>, Void, Void> {
        UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(List<User>... lists) {
            userDao.insertUsers(lists[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        UserDao userDao;
        private DeleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

}
