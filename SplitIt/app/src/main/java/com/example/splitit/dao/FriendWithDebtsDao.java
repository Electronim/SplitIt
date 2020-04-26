package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.splitit.model.FriendWithDebts;

import java.util.List;

@Dao
public interface FriendWithDebtsDao {

    @Transaction
    @Query("Select * from Friend")
    List<FriendWithDebts> getAllFriendsWithDebts();

    @Transaction
    @Query("Select * from Friend where id = :id limit 1")
    FriendWithDebts getFriendWithDebtsById(long id);
}
