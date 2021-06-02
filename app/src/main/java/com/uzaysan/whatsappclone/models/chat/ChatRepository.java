package com.uzaysan.whatsappclone.models.chat;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.uzaysan.whatsappclone.database.AppDatabase;
import com.uzaysan.whatsappclone.models.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRepository {

    private final LiveData<List<Chat>> allChats;
    private final ChatDao chatDao;

    public ChatRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        chatDao = appDatabase.chatDao();
        allChats = chatDao.getAllChats();
    }

    public LiveData<Chat> getChatById(String id) {
        return chatDao.getChatById(id);
    }

    public void updateMembers(List<User> members) {
        new UpdateMembersAsyncTask(chatDao).execute(members);
    }

    public void insert(Chat chat) {
        List<Chat> chats = new ArrayList<>();
        chats.add(chat);
        new InsertChatAsyncTask(chatDao).execute(chats);
    }

    public void insertAll(List<Chat> chats) {
        new InsertChatAsyncTask(chatDao).execute(chats);
    }

    public void update(Chat chat) {
        List<Chat> chats = new ArrayList<>();
        chats.add(chat);
        new UpdateChatAsyncTask(chatDao).execute(chats);
    }

    public void updateAll(List<Chat> chats) {
        new UpdateChatAsyncTask(chatDao).execute(chats);
    }

    public void delete(Chat chat) {
        List<Chat> chats = new ArrayList<>();
        chats.add(chat);
        new DeleteChatAsyncTask(chatDao).execute(chats);
    }

    public void deleteAll() {
        new DeleteAllChatAsyncTask(chatDao).execute();
    }

    public LiveData<List<Chat>> getAllChats() {
        return allChats;
    }

    private static class InsertChatAsyncTask extends AsyncTask<List<Chat>, Void, Void> {
        ChatDao chatDao;
        private InsertChatAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }
        @Override
        protected Void doInBackground(List<Chat>... lists) {
            chatDao.insertAllChats(lists[0]);
            return null;
        }
    }

    private static class UpdateChatAsyncTask extends AsyncTask<List<Chat>, Void, Void> {
        ChatDao chatDao;
        private UpdateChatAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }
        @Override
        protected Void doInBackground(List<Chat>... lists) {
            chatDao.updateAllChats(lists[0]);
            return null;
        }
    }

    private static class DeleteChatAsyncTask extends AsyncTask<List<Chat>, Void, Void> {
        ChatDao chatDao;
        private DeleteChatAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }
        @Override
        protected Void doInBackground(List<Chat>... lists) {
            chatDao.deleteAllChats(lists[0]);
            return null;
        }
    }

    private static class DeleteAllChatAsyncTask extends AsyncTask<Void, Void, Void> {
        ChatDao chatDao;
        private DeleteAllChatAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            chatDao.deleteAllChats();
            return null;
        }
    }

    private static class UpdateMembersAsyncTask extends AsyncTask<List<User>, Void, Void> {

        ChatDao chatDao;

        private UpdateMembersAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }

        @Override
        protected Void doInBackground(List<User>... lists) {
            chatDao.updateMembers(lists[0]);
            return null;
        }
    }
}
