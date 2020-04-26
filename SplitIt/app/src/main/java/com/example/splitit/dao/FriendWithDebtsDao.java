package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.splitit.model.FriendWithDebts;

import java.util.List;

@Dao
public interface FriendWithDebtsDao {

    @Query("Select * from Friend")
    List<FriendWithDebts> getAllFriendsWithDebts();

    @Query("Select * from Friend where id = :id limit 1")
    FriendWithDebts getFriendWithDebtsById(long id);
}
