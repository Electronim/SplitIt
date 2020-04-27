package com.example.splitit.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"group_id", "friend_id"})
public class GroupFriendCrossRef {
    public long group_id;
    public long friend_id;

    public GroupFriendCrossRef(long group_id, long friend_id) {
        this.group_id = group_id;
        this.friend_id = friend_id;
    }
}

