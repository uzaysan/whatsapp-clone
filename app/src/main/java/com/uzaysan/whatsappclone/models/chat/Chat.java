package com.uzaysan.whatsappclone.models.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.uzaysan.whatsappclone.helper.TypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity(tableName = "chat_table")
public class Chat {

    private long updatedAt;
    private boolean isGroupChat;
    private String members;
    private String lastMessage;
    String chatIcon, chatName;



    @PrimaryKey
    @NonNull
    private String id;

    public Chat(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.isGroupChat = (boolean) data.get("is_group_chat");
        this.chatIcon =(String) data.get("chat_icon");
        this.chatName =(String) data.get("chat_name");
        this.lastMessage = (String) data.get("last_message");
        this.members = TypeConverter.stringFromArrayList((ArrayList<String>) data.get("members"));
        this.updatedAt = ((Timestamp) data.get("updated_at")).toDate().getTime();
    }


    public Chat() {
    }

    public String getChatIcon() {
        return chatIcon;
    }

    public void setChatIcon(String chatIcon) {
        this.chatIcon = chatIcon;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public String getMembers() {
        return members;
    }


    public void setMembers(String members) {
        this.members = members;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Chat) {
           return getId().equals(((Chat) obj).getId());
        }
        return false;
    }


}
