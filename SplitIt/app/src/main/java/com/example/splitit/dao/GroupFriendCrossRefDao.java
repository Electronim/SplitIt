package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.splitit.model.GroupFriendCrossRef;

@Dao
public interface GroupFriendCrossRefDao {
    @Insert
    void insertGroupFriend(GroupFriendCrossRef groupFriendCrossRef);

    @Delete
    void deleteGroupFriend(GroupFriendCrossRef groupFriendCrossRef);
}
