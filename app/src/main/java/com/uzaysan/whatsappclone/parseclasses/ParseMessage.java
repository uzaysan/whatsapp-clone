package com.uzaysan.whatsappclone.parseclasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class ParseMessage extends ParseObject {

    //Getters
    public String getOwner() {
        return getString("owner");
    }

    public String getMessage() {
        return getString("message");
    }

    public String getChat() {
        return getString("chat");
    }

    //Setters

    public void setMessage(String message) {
        put("message", message);
    }

    public void setChat(String chat) {
        put("chat", chat);
    }

    public void setOwner(String owner) {
        put("owner", owner);
    }
}
