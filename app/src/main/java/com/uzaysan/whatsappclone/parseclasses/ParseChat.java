package com.uzaysan.whatsappclone.parseclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Chat")
public class ParseChat extends ParseObject {

    public List<String> getMembers() {
        return getList("members");
    }

    public boolean getIsGroupChat() {
        return getBoolean("is_group_chat");
    }

    public String getLastMessage() {
        return getString("last_message");
    }

    public Date getLastMessageDate() {
        return getDate("last_message_date");
    }

    public String getChatIconURL() {
        return getParseFile("chat_icon") != null ? getParseFile("chat_icon").getUrl() : "";
    }

    public String getChatName() {
        return getString("chat_name");
    }

    //Setters

    public synchronized void addMember(ParseUser user) {
        List<String> current = getList("members");
        current.add(user.getObjectId());
        put("members",current);
    }

    public synchronized void addMember(List<ParseUser> users) {
        List<String> current = getList("members");
        List<String> tmp = new ArrayList<>();
        for (ParseUser user : users) {
            tmp.add(user.getObjectId());
        }
        current.addAll(tmp);
        put("members",current);
    }

    public void setIsGroupChat() {
        put("is_group_chat",true);
    }

    public void setLastMessage(String message) {
        put("last_message",message);
    }

    public void setLastMessageDate(Date date) {
        put("last_message_date", date);
    }

    public void setChatIconURL(ParseFile icon) {
        put("chat_icon", icon);
    }

    public void setChatName(String name) {
        put("chat_name", name);
    }

}
