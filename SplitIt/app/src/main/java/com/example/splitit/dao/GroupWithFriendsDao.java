package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.splitit.model.GroupWithFriends;

import java.util.List;

@Dao
public interface GroupWithFriendsDao {

    @Transaction
    @Query("Select * from Groups")
    List<GroupWithFriends> getAllGroupsWithFriends();

    @Transaction
    @Query("Select * from Groups where groupId = :id limit 1")
    GroupWithFriends getGroupWithFriendsById(long id);
}
