package com.uzaysan.whatsappclone.models.message;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.uzaysan.whatsappclone.database.AppDatabase;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatDao;
import com.uzaysan.whatsappclone.models.chat.ChatRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageRepository {

    LiveData<List<Message>> messages;
    MessageDao messageDao;
    List<Message> messageList = new ArrayList<>();

    public MessageRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.messageDao = appDatabase.messageDao();

    }

    public LiveData<List<Message>> getMessages(String chat) {
        this.messages = messageDao.getMessages(chat);
        return messages;
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
