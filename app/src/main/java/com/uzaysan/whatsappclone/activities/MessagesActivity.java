 package com.uzaysan.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.quarkworks.roundedframelayout.RoundedFrameLayout;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.adapters.ChatsAdapter;
import com.uzaysan.whatsappclone.adapters.MessagesAdapter;
import com.uzaysan.whatsappclone.helper.TypeConverter;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.message.Message;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;
import com.uzaysan.whatsappclone.viewmodels.ChatViewModel;
import com.uzaysan.whatsappclone.viewmodels.MessageViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

 public class MessagesActivity extends AppCompatActivity implements View.OnClickListener, Observer<Chat> {

    ChatViewModel chatViewModel;
    CircleImageView chatIcon;
    TextView chatName, chatMembers;
    RelativeLayout chatLayout, addImage;
    RoundedFrameLayout sendButton;
    Chat chat;
    EditText chatInput;

    MessagesAdapter adapter;
    MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(this);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel
                .getChatById(getIntent().getStringExtra("chat_id"))
                .observe(this,this);

        chatIcon = findViewById(R.id.chat_icon);
        chatName = findViewById(R.id.chat_name);
        chatMembers = findViewById(R.id.chat_members);

        chatLayout = findViewById(R.id.chat_layout);
        chatLayout.setOnClickListener(this);

        addImage = findViewById(R.id.camera_icon);
        addImage.setOnClickListener(this);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        adapter = new MessagesAdapter(Glide.with(this));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        chatInput = findViewById(R.id.chatInput);

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);


    }

     @Override
     public void onClick(View v) {
        if(v.getId() == R.id.back) {
            onBackPressed();
        } else if(v.getId() == R.id.camera_icon) {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
        } else if(v.getId() == R.id.send_button) {
            //Toast.makeText(this, "Send clicked", Toast.LENGTH_SHORT).show();
            if(chat == null) return;
            ParseMessage message = new ParseMessage();
            message.setChat(chat.getId());
            message.setMessage(chatInput.getText().toString().trim());
            message.setOwner(ParseUser.getCurrentUser().getObjectId());
            message.saveInBackground();
            chatInput.setText("");


        }
     }

     @Override
     public void onBackPressed() {
         super.onBackPressed();
     }

     @Override
     public void onChanged(Chat chat) {
         setChat(chat);
         Glide.with(this)
                 .load(chat.getChatIcon())
                 .into(chatIcon);
         chatName.setText(chat.getChatName());
         chatMembers.setText(TypeConverter.arrayFromString(chat.getMembers()).size()+ " members");
     }

     private synchronized void setChat(Chat chat) {
        if(this.chat == null) {
            this.chat = chat;
            messageViewModel.getMessageLiveData(chat.getId()).observe(this, new Observer<List<Message>>() {
                @Override
                public void onChanged(List<Message> messageList) {
                    adapter.setData(messageList);
                }
            });
        }
     }
 }