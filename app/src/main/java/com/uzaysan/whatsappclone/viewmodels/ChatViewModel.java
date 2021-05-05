package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.uzaysan.whatsappclone.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<List<Chat>> chatLiveData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        this.chatLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Chat>> getChats() {
        return this.chatLiveData;
    }

    public void setUpData(int size) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                List<Chat> data = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    data.add(new Chat());
                }
                ChatViewModel.this.chatLiveData.postValue(data);

            }
        }, 5000);
    }




}
