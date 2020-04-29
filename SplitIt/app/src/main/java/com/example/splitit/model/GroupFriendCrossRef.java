package com.example.splitit.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"groupId", "friendId"})
public class GroupFriendCrossRef {
    public long groupId;
    public long friendId;

    public GroupFriendCrossRef(long groupId, long friendId) {
        this.groupId = groupId;
        this.friendId = friendId;
    }
}

