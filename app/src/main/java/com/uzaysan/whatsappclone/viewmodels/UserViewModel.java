package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.models.user.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;

public class UserViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private final UserRepository userRepository;
    private String id;
    ParseLiveQueryClient parseLiveQueryClient;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<User> getUserLiveData(String id) {
        userLiveData = userRepository.getUserLiveData(id);
        this.id = id;
        return this.userLiveData;
    }

    public void startListen() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ParseUser> parseQuery = ParseQuery.getQuery(ParseUser.class);
        parseQuery.whereEqualTo("objectId",id);

        SubscriptionHandling<ParseUser> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseUser>() {
            @Override
            public void onEvents(ParseQuery<ParseUser> query, SubscriptionHandling.Event event, ParseUser object) {
                if(object!=null) {
                    //Toast.makeText(getApplication().getApplicationContext(), "Update recieved", Toast.LENGTH_SHORT).show();
                    userRepository.insertUser(new User(object));
                }
            }
        });

    }
}
