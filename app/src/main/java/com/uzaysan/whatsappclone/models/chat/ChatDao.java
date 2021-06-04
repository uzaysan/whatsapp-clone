package com.uzaysan.whatsappclone.models.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.uzaysan.whatsappclone.models.user.User;

import java.util.List;

@Dao
public interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat(Chat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateChat(Chat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAllChats(List<Chat> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateMembers(List<User> users);

    @Delete
    void deleteChat(Chat chat);

    @Delete
    void deleteAllChats(List<Chat> chats);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllChats(List<Chat> chats);

    @Query("DELETE FROM chat_table")
    void deleteAllChats();

    @Query("SELECT * FROM chat_table ORDER BY lastMessageDate DESC")
    LiveData<List<Chat>> getAllChats();

    @Query("SELECT * FROM chat_table WHERE id=:id")
    LiveData<Chat> getChatById(String id);
}
