package com.example.splitit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class FriendWithDebts implements Serializable {
    @Embedded
    public Friend friend;
    @Relation(
            parentColumn = "friend_id",
            entityColumn = "friendId"
    )
    public List<Debt> debts;
}