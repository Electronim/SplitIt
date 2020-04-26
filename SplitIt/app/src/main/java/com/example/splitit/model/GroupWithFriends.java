package com.example.splitit.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class GroupWithFriends {
    @Embedded
    public Group group;
    @Relation(
            parentColumn = "group_id",
            entityColumn = "friend_id",
            associateBy = @Junction(GroupFriendCrossRef.class)
    )
    public List<Friend> friends;
}
