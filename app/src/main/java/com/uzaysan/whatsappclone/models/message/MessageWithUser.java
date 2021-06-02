package com.uzaysan.whatsappclone.models.message;


import com.uzaysan.whatsappclone.models.user.User;

public class MessageWithUser {

    private User user;
    private Message message;

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

}
