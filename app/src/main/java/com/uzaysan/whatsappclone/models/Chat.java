package com.uzaysan.whatsappclone.models;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Chat {

    private Date updatedAt;
    private boolean isGroupChat;
    private List<String> members;
    private String lastMessage;
    private ChatInfo chatInfo;
    private String id;

    public Chat(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.isGroupChat = (boolean) data.get("is_group_chat");
        this.chatInfo = new ChatInfo((Map<String, Object>) data.get("chat_info"));
        this.lastMessage = (String) data.get("last_message");
        this.members = (List<String>) data.get("members");
        this.updatedAt = ((Timestamp) data.get("updated_at")).toDate();
    }

    public Chat() {

    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
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

    public class ChatInfo {
        String chatName;
        String chatIcon;

        public ChatInfo(Map<String, Object> chat_info) {
            this.chatIcon = (String) chat_info.get("chat_icon");
            this.chatName = (String) chat_info.get("chat_name");
        }

        public String getChatName() {
            return chatName;
        }

        public void setChatName(String chatName) {
            this.chatName = chatName;
        }

        public String getChatIcon() {
            return chatIcon;
        }

        public void setChatIcon(String chatIcon) {
            this.chatIcon = chatIcon;
        }
    }
}
