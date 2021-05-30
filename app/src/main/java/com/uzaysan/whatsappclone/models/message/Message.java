package com.uzaysan.whatsappclone.models.message;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.parseclasses.ParseMessage;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "message_table")
public class Message {

    @NotNull
    @PrimaryKey
    String id;

    String message, owner_name, owner_profile_photo, chat, owner;
    long created_at;

    public Message(ParseMessage data) {
        this.id = data.getObjectId();
        this.chat = data.getChat();
        this.message = data.getMessage();
        this.owner = data.getOwner();
        this.created_at = data.getCreatedAt().getTime();
        //this.owner_name = user.getName();
        //this.owner_profile_photo = user.getProfile_photo();
    }

    public Message() {}

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_profile_photo() {
        return owner_profile_photo;
    }

    public void setOwner_profile_photo(String owner_profile_photo) {
        this.owner_profile_photo = owner_profile_photo;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
