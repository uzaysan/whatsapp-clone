package com.uzaysan.whatsappclone.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uzaysan.whatsappclone.models.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatViewModel extends AndroidViewModel implements EventListener<QuerySnapshot> {

    private final MutableLiveData<List<Chat>> chatLiveData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        this.chatLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Chat>> getChats() {
        return this.chatLiveData;
    }

    public void setUpData(int size) {
        CollectionReference chatRef = FirebaseFirestore.getInstance().collection("Chats");
        chatRef.limit(size);
        chatRef.addSnapshotListener(this);
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
        this.chatLiveData.postValue(data);
    }
}
