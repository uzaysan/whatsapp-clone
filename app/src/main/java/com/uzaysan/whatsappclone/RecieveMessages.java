package com.uzaysan.whatsappclone;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.uzaysan.whatsappclone.data.ChatRepository;
import com.uzaysan.whatsappclone.data.MessageRepository;
import com.uzaysan.whatsappclone.models.Chat;
import com.uzaysan.whatsappclone.models.Message;
import com.uzaysan.whatsappclone.parseclasses.ParseChat;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

import java.util.HashMap;
import java.util.Map;

public class RecieveMessages extends FirebaseMessagingService {

    MessageRepository messageRepository;
    ChatRepository chatRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        messageRepository = new MessageRepository(this);
        chatRepository = new ChatRepository(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null) return;

        Map<String, String> data = remoteMessage.getData();
        if (data == null) return;

        String id = data.get("id");
        String type = data.get("type");
        if (id == null || type == null) return;

        if (type.equals("message")) new GetMessage(messageRepository, id).execute();
        else if (type.equals("chat")) new GetChat(chatRepository, id).execute();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Map<String, String> params = new HashMap<>();
        params.put("token", s);
        ParseCloud.callFunctionInBackground("updateToken",params);
    }

    private static class GetMessage extends AsyncTask<Void, Void, Void> {
        MessageRepository messageRepository;
        String id;

        public GetMessage(MessageRepository messageRepository, String id) {
            this.messageRepository = messageRepository;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ParseQuery<ParseMessage> getMessage = new ParseQuery<>(ParseMessage.class);
                ParseMessage message = getMessage.get(id);
                messageRepository.insert(new Message(message));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetChat extends AsyncTask<Void, Void, Void> {
        ChatRepository chatRepository;
        String id;

        public GetChat(ChatRepository chatRepository, String id) {
            this.chatRepository = chatRepository;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ParseQuery<ParseChat> getMessage = new ParseQuery<>(ParseChat.class);
                ParseChat chat = getMessage.get(id);
                chatRepository.insert(new Chat(chat));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
