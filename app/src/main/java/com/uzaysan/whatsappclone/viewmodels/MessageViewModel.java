package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.message.Message;
import com.uzaysan.whatsappclone.models.message.MessageDao;
import com.uzaysan.whatsappclone.models.message.MessageRepository;
import com.uzaysan.whatsappclone.models.message.MessageWithUser;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.models.user.UserDao;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageViewModel extends AndroidViewModel {

    public static final int TYPE_ADD_END = 1;
    public static final int TYPE_ADD_START = -1;
    public static final int TYPE_INIT = 0;

    ParseLiveQueryClient parseLiveQueryClient;
    MessageRepository messageRepository;
    MutableLiveData<List<Message>> messageLiveData;
    MutableLiveData<List<MessageWithUser>> messageWithUserLiveData;


    MessageDao messageDao;
    UserDao userDao;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application.getApplicationContext());
        messageDao = messageRepository.getMessageDao();
        userDao = messageRepository.getUserDao();
    }

    public MutableLiveData<List<Message>> getMessageLiveData(String chat) {
        //messageLiveData = messageRepository.getMessages(chat);
        messageLiveData = new MutableLiveData<>();
        startListen(chat);
        return messageLiveData;

    }

    public MutableLiveData<List<MessageWithUser>> getMessageWithUserLiveData() {
        messageWithUserLiveData = new MutableLiveData<>();

        return messageWithUserLiveData;
    }

    public LiveData<List<Message>> getRealtimeUpdates(String chat) {
        return messageRepository.getMessages(chat);
    }


    public void loadMessages(String chat, long date) {
        new GetMessages(messageLiveData,date,chat,messageDao, userDao, messageWithUserLiveData).execute();
    }

    public void startListen(String chat) {


        ParseQuery<ParseMessage> getMessages = new ParseQuery<ParseMessage>(ParseMessage.class);
        getMessages.whereEqualTo("chat",chat);
        getMessages.setLimit(1000);
        getMessages.findInBackground(new FindCallback<ParseMessage>() {
            @Override
            public void done(List<ParseMessage> objects, ParseException e) {
                if(e != null) return;

                List<Message> messages = new ArrayList<>();
                for(ParseMessage message : objects) {
                    messages.add(new Message(message));
                }
                messageRepository.insertAll(messages);
            }
        });

        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ParseMessage> parseQuery = new ParseQuery<ParseMessage>(ParseMessage.class);
        parseQuery.whereEqualTo("chat",chat);

        SubscriptionHandling<ParseMessage> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseMessage>() {
            @Override
            public void onEvents(ParseQuery<ParseMessage> query, SubscriptionHandling.Event event, ParseMessage object) {
                if(object != null) messageRepository.insert(new Message(object));
            }
        });


    }

    private static class GetMessages extends AsyncTask<Void, Void, Void> {

        MutableLiveData<List<Message>> messageLiveData;
        long date;
        String chat;
        MessageDao messageDao;
        UserDao userDao;
        MutableLiveData<List<MessageWithUser>> messageWithUserLiveData;
        public GetMessages(MutableLiveData<List<Message>> messageLiveData
                , long date
                , String chat
                , MessageDao messageDao
                , UserDao userDao
                , MutableLiveData<List<MessageWithUser>> messageWithUserLiveData) {
            this.messageLiveData = messageLiveData;
            this.date = date;
            this.chat = chat;
            this.messageDao = messageDao;
            this.userDao = userDao;
            this.messageWithUserLiveData = messageWithUserLiveData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(date == 0) {
                List<Message> messages = messageDao.getMessagesInit(chat);
                List<String> ids = new ArrayList<>();
                for(Message message : messages) {
                    ids.add(message.getOwner());
                }
                List<User> users = userDao.getUsersById(ids);
                List<MessageWithUser> result = new ArrayList<>();
                for(int i = users.size() - 1; i >= 0; i--) {
                    for(int j = messages.size() - 1; j >= 0; j--) {
                        if(messages.get(j).getOwner().equals(users.get(i).getId())) {
                            result.add(0, new MessageWithUser(users.get(i),messages.get(j)));
                            messages.remove(j);
                        }
                    }
                }

                messageWithUserLiveData.postValue(result);
                messageLiveData.postValue(messages);
                return null;
            }
            List<Message> messages = messageDao.getMessagesBeforeDate(chat, date);
            messageLiveData.postValue(messages);
            return null;
        }

    }

}
