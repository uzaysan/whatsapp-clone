package com.uzaysan.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("47PzvRyV6edp")
                .server("http://media.uzaysan.xyz:1337/parse/")
                .build()
        );
    }
}
