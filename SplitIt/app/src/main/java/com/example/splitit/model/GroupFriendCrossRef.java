package com.example.splitit.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"group_id", "friend_id"})
public class GroupFriendCrossRef {
    public long group_id;
    public long friend_id;
}

