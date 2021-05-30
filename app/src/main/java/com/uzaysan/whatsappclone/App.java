package com.uzaysan.whatsappclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ParseChat.class);
        ParseObject.registerSubclass(ParseMessage.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("47PzvRyV6edp")
                .server("http://media.uzaysan.xyz:1337/parse/")
                .build()
        );
    }
}
