package com.uzaysan.whatsappclone.models.message;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.uzaysan.whatsappclone.database.AppDatabase;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatDao;
import com.uzaysan.whatsappclone.models.chat.ChatRepository;
import com.uzaysan.whatsappclone.models.user.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageRepository {

    MessageDao messageDao;
    UserDao userDao;


    public MessageRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.messageDao = appDatabase.messageDao();
        this.userDao = appDatabase.userDao();

    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public LiveData<List<Message>> getMessages(String chat) {
        return messageDao.getMessages(chat);
    }

    public void insert(Message message) {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        new InsertMessageAsyncTask(messageDao).execute(messages);
    }

    public void insertAll(List<Message> messages) {
        new InsertMessageAsyncTask(messageDao).execute(messages);
    }

    private static class InsertMessageAsyncTask extends AsyncTask<List<Message>, Void, Void> {
        MessageDao messageDao;
        private InsertMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }
        @Override
        protected Void doInBackground(List<Message>... lists) {
            messageDao.insertMessages(lists[0]);
            return null;
        }
    }


}
