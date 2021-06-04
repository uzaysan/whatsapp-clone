package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.models.user.UserDao;
import com.uzaysan.whatsappclone.models.user.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    UserDao userDao;
    ParseLiveQueryClient parseLiveQueryClient;
    MutableLiveData<List<User>> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userDao = userRepository.getUserDao();

    }

    public MutableLiveData<List<User>> getUserLiveData() {
        userLiveData = new MutableLiveData<>();
        return userLiveData;
    }

    public LiveData<User> getUserLiveData(String id) {
        startListen(id);
        return userRepository.getUserLiveData(id);
    }

    public void getUsersByQuery(String query) {
        new GetUserByQuery(query, userDao, userLiveData).execute();
    }

    public void startListen(String id) {
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

    private static class GetUserByQuery extends AsyncTask<Void, Void, Void> {
        String query;
        UserDao userDao;
        MutableLiveData<List<User>> userLiveData;

        public GetUserByQuery(String query, UserDao userDao, MutableLiveData<List<User>> userLiveData) {
            this.query = query;
            this.userDao = userDao;
            this.userLiveData = userLiveData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String sqlQuery = "%" + query + "%";
            List<User> users = userDao.getUserByString(sqlQuery, ParseUser.getCurrentUser().getObjectId());
            userLiveData.postValue(users);
            try {
                ParseQuery<ParseUser> getUsers = new ParseQuery<ParseUser>(ParseUser.class);
                getUsers.whereStartsWith("username", query);
                getUsers.setLimit(50);
                List<ParseUser> parseUsers = getUsers.find();

                ParseQuery<ParseUser> getUsers2 = new ParseQuery<ParseUser>(ParseUser.class);
                getUsers2.whereStartsWith("name", query);
                getUsers2.setLimit(50);
                List<ParseUser> parseUsers2 = getUsers2.find();

                parseUsers.addAll(parseUsers2);

                List<User> newUsers = new ArrayList<>();
                for(ParseUser user : parseUsers) {
                    newUsers.add(new User(user));
                }

                userDao.insertUsers(newUsers);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<User> users2 = userDao.getUserByString(sqlQuery, ParseUser.getCurrentUser().getObjectId());
            userLiveData.postValue(users2);
            return null;
        }
    }
}
