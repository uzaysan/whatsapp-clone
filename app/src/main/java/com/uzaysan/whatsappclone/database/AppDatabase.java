package com.uzaysan.whatsappclone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.chat.ChatDao;
import com.uzaysan.whatsappclone.models.message.Message;
import com.uzaysan.whatsappclone.models.message.MessageDao;
import com.uzaysan.whatsappclone.models.user.User;
import com.uzaysan.whatsappclone.models.user.UserDao;

@Database(entities = {Chat.class, User.class, Message.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ChatDao chatDao();
    public abstract UserDao userDao();
    public abstract MessageDao messageDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , AppDatabase.class
                    ,"app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
