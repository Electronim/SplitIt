package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.splitit.model.Friend;

import java.util.List;

@Dao
public interface FriendDao {

    @Query("Select * from Friend")
    List<Friend> getAllFriends();

    @Query("Select * from Friend where friendId = :id limit 1")
    Friend getFriendById(long id);

    @Insert
    void insertFriend(Friend friend);

    @Delete
    void deleteFriend(Friend friend);
}
