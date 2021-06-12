package com.uzaysan.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.adapters.ChatsAdapter;
import com.uzaysan.whatsappclone.models.Chat;
import com.uzaysan.whatsappclone.viewmodels.ChatViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ChatsAdapter.ChatAdapterClickListener
        , Observer<List<Chat>> {

    ChatViewModel chatViewModel;
    ChatsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ParseUser.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        adapter = new ChatsAdapter(Glide.with(this));
        adapter.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getChats().observe(this,this);

        FrameLayout addNewChat = findViewById(R.id.addNewChat);
        addNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewChatSearchUserActivity.class));
            }
        });

    }

    @Override
    public void onChanged(List<Chat> chats) {
        adapter.setData(chats);
    }

    @Override
    public void onClick(String id) {
        startActivity(new Intent(this, MessagesActivity.class).putExtra("chat_id",id));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}