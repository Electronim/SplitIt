package com.example.splitit.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.Group;
import com.example.splitit.model.GroupFriendCrossRef;

@Database(entities = {Friend.class, Debt.class, Group.class, GroupFriendCrossRef.class, Action.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FriendDao friendDao();
    public abstract DebtDao debtDao();
    public abstract FriendWithDebtsDao friendWithDebtsDao();
    public abstract GroupWithFriendsDao groupWithFriendsDao();
    public abstract ActionDao actionDao();

}
