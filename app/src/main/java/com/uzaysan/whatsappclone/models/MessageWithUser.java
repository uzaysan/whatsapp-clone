package com.uzaysan.whatsappclone.models;


public class MessageWithUser {

    private User user;
    private Message message;
    private int type;

    public static final int TYPE_LOAD = 1453;

    public MessageWithUser(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
