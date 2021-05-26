package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;

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
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e != null) return;
                userRepository.insertUser(new User((ParseUser) object));
            }
        });
    }
}
