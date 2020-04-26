package com.example.splitit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FriendWithDebts {
    @Embedded
    public Friend friend;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<Friend> detbs;
}