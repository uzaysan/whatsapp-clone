package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.Message;
import com.uzaysan.whatsappclone.data.MessageDao;
import com.uzaysan.whatsappclone.data.MessageRepository;
import com.uzaysan.whatsappclone.models.MessageWithUser;
import com.uzaysan.whatsappclone.models.User;
import com.uzaysan.whatsappclone.data.UserDao;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageViewModel extends AndroidViewModel {

    public static final int TYPE_ADD_END = 1;
    public static final int TYPE_ADD_START = -1;

    MessageRepository messageRepository;
    MutableLiveData<Map<String, Object>> messageWithUserLiveData;


    MessageDao messageDao;
    UserDao userDao;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application.getApplicationContext());
        messageDao = messageRepository.getMessageDao();
        userDao = messageRepository.getUserDao();
    }

    public MutableLiveData<Map<String, Object>> getMessageWithUserLiveData() {
        messageWithUserLiveData = new MutableLiveData<>();
        return messageWithUserLiveData;
    }

    public LiveData<List<Message>> getRealtimeUpdates(String chat) {
        /*ParseQuery<ParseMessage> parseQuery = new ParseQuery<>(ParseMessage.class);
        parseQuery.whereEqualTo("chat",chat);
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        SubscriptionHandling<ParseMessage> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseMessage>() {
            @Override
            public void onEvents(ParseQuery<ParseMessage> query, SubscriptionHandling.Event event, ParseMessage object) {
                if(object != null) messageRepository.insert(new Message(object));
            }
        });*/
        return messageRepository.getMessages(chat);
    }


    public void loadMessages(String chat, long date) {
        new GetMessages(date,chat,messageDao, userDao, messageWithUserLiveData).execute();
    }

    public void loadSingleMessage(Message message) {
        new GetSingleMessage(message, userDao, messageWithUserLiveData).execute();
    }

    private static class GetMessages extends AsyncTask<Void, Void, Void> {

        long date;
        String chat;
        MessageDao messageDao;
        UserDao userDao;
        MutableLiveData<Map<String, Object>> messageWithUserLiveData;
        public GetMessages(long date
                , String chat
                , MessageDao messageDao
                , UserDao userDao
                , MutableLiveData<Map<String, Object>> messageWithUserLiveData) {
            this.date = date;
            this.chat = chat;
            this.messageDao = messageDao;
            this.userDao = userDao;
            this.messageWithUserLiveData = messageWithUserLiveData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Message> messages = new ArrayList<>();
            try {
                if (date == 0) {
                    messages = messageDao.getMessagesInit(chat);
                } else {
                    messages = messageDao.getMessagesBeforeDate(chat, date);
                }
                List<MessageWithUser> result = new ArrayList<>();
                for (int i = 0; i < messages.size() - 1; i++) {
                    Message message = messages.get(i);
                    User user = userDao.getUserByIdSync(message.getOwner());
                    if (user != null) {
                        result.add(new MessageWithUser(user, message));
                        continue;
                    }
                    ParseQuery<ParseUser> getUser = new ParseQuery<>(ParseUser.class);
                    ParseUser parseUser = getUser.get(message.getOwner());
                    User userNew = new User(parseUser);
                    userDao.insertUser(userNew);
                    result.add(new MessageWithUser(userNew, message));
                }
                Map<String, Object> map = new HashMap<>();
                map.put("direction", TYPE_ADD_END);
                map.put("list", result);

                messageWithUserLiveData.postValue(map);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private static class GetSingleMessage extends AsyncTask<Void, Void, Void> {

        Message message;
        UserDao userDao;
        MutableLiveData<Map<String, Object>> messageWithUserLiveData;

        public GetSingleMessage(Message message, UserDao userDao, MutableLiveData<Map<String, Object>> messageWithUserLiveData) {
            this.message = message;
            this.userDao = userDao;
            this.messageWithUserLiveData = messageWithUserLiveData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = userDao.getUserByIdSync(message.getOwner());
            List<MessageWithUser> result = new ArrayList<>();
            if (user != null) {
                result.add(new MessageWithUser(user, message));
            }
            try {
                ParseQuery<ParseUser> getUser = new ParseQuery<ParseUser>(ParseUser.class);
                ParseUser parseUser = getUser.get(message.getOwner());
                User userNew = new User(parseUser);
                userDao.insertUser(userNew);
                result.add(new MessageWithUser(userNew, message));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("direction",TYPE_ADD_START);
            map.put("list",result);
            messageWithUserLiveData.postValue(map);
            return null;
        }
    }

}
