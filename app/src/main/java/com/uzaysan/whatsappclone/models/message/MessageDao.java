package com.uzaysan.whatsappclone.models.message;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> message);

    @Delete
    void deleteMessage(Message message);

    @Query("DELETE FROM message_table")
    void deleteAllMessages();

    @Query("SELECT * FROM message_table WHERE chat=:chat ORDER BY created_at DESC LIMIT 1000")
    LiveData<List<Message>> getMessages(String chat);


}
