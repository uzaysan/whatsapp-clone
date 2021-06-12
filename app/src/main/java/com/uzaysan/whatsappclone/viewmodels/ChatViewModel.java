package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.Chat;
import com.uzaysan.whatsappclone.data.ChatRepository;
import com.uzaysan.whatsappclone.models.User;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatViewModel extends AndroidViewModel implements EventListener<QuerySnapshot> {

    ParseLiveQueryClient parseLiveQueryClient;
    private final ChatRepository chatRepository;

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
        return chatRepository.getChatById(id);

    }

    public void updateMembers(List<String> members) {
        ParseQuery<ParseUser> getMembers = new ParseQuery<ParseUser>(ParseUser.class);
        getMembers.whereContainedIn("objectId",members);
        getMembers.setLimit(members.size());
        getMembers.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null) return;
                List<User> users = new ArrayList<>();
                for(ParseUser user : objects) {
                    users.add(new User(user));
                }
                chatRepository.updateMembers(users);
            }
        });

    }


    public LiveData<List<Chat>> getChats() {
        return chatRepository.getAllChats();
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
