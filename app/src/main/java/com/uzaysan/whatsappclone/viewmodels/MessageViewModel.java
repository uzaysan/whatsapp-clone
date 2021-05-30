package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;

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
import com.uzaysan.whatsappclone.models.message.MessageRepository;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    public static final int DIRECTION_END = 1;
    public static final int DIRECTION_START = -1;
    ParseLiveQueryClient parseLiveQueryClient;
    MessageRepository messageRepository;
    LiveData<List<Message>> messageLiveData;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application.getApplicationContext());
    }

    public LiveData<List<Message>> getMessageLiveData(String chat) {
        messageLiveData = messageRepository.getMessages(chat);
        startListen(chat);
        return messageLiveData;

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

}
