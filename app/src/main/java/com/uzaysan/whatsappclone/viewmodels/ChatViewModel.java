package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatRepository;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatViewModel extends AndroidViewModel implements EventListener<QuerySnapshot> {

    private LiveData<List<Chat>> chatLiveData;
    ParseLiveQueryClient parseLiveQueryClient;
    private LiveData<Chat> chat;
    private final ChatRepository chatRepository;
    CollectionReference chatRef;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository = new ChatRepository(application);

    }

    public LiveData<Chat> getChatById(String id) {
        ParseQuery<ParseChat> getChats = new ParseQuery<ParseChat>(ParseChat.class);
        getChats.getInBackground(id, new GetCallback<ParseChat>() {
            @Override
            public void done(ParseChat object, ParseException e) {
                if(e == null) {
                    chatRepository.insert(new Chat(object));
                }
            }
        });
        chat = chatRepository.getChatById(id);
        return chat;
    }


    public LiveData<List<Chat>> getChats() {
        this.chatLiveData = chatRepository.getAllChats();
        return this.chatLiveData;
    }

    public void listenData() {
        ParseQuery<ParseChat> parseQuery = ParseQuery.getQuery(ParseChat.class);
        List<String> ids = new ArrayList<>();
        ids.add(ParseUser.getCurrentUser().getObjectId());

        ParseQuery<ParseChat> getChats = new ParseQuery<ParseChat>(ParseChat.class);
        getChats.whereContainedIn("members",ids);
        getChats.setLimit(1000);
        getChats.findInBackground(new FindCallback<ParseChat>() {
            @Override
            public void done(List<ParseChat> objects, ParseException e) {
                if(e != null) return;

                List<Chat> chats = new ArrayList<>();
                for(ParseChat chat : objects) {
                    chats.add(new Chat(chat));
                }
                chatRepository.insertAll(chats);
            }
        });


        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        parseQuery.whereContainedIn("members",ids);

        SubscriptionHandling<ParseChat> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseChat>() {
            @Override
            public void onEvents(ParseQuery<ParseChat> query, SubscriptionHandling.Event event, ParseChat object) {
                if(object != null) {
                    chatRepository.insert(new Chat(object));
                }
            }
        });

        /*chatRef = FirebaseFirestore.getInstance().collection("Chats");
        chatRef.limit(25);
        chatRef.addSnapshotListener(this);*/
    }


    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            Log.e("Error while fetching", error.getMessage());
            return;
        }

        DocumentSnapshot qds = value.getDocuments().get(0);
        Map<String, Object> tmpData = qds.getData();
        tmpData.put("id",qds.getId());

        List<Chat> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new Chat(tmpData));
        }
        this.chatRepository.insertAll(data);
    }
}
