package com.uzaysan.whatsappclone.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.parse.ParseUser;

@Entity(tableName = "user_table")
public class User {

    String username, name, status, profile_photo;

    @NonNull
    @PrimaryKey
    String id;

    @Ignore
    public User(ParseUser user) {
        this.id = user.getObjectId();
        this.username = user.getUsername();
        this.name = user.getString("name") != null ? user.getString("name") : "";
        this.status = user.getString("status") != null ? user.getString("status") : "";
        this.profile_photo = user.getParseFile("profile_photo") != null ? user.getParseFile("profile_photo").getUrl() : "";
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean res = false;
        if(obj instanceof User) {
            if(((User)obj).getId().equals(this.id)) res = true;
        }
        return res;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
