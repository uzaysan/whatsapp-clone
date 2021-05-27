package com.uzaysan.whatsappclone.models.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("SELECT * FROM user_table WHERE id=:id")
    LiveData<User> getUserById(String id);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();
}