package com.example.splitit.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.splitit.model.Friend;

@Database(entities = {Friend.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FriendDao friendDao();

}
