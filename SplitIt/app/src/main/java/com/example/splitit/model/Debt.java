package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Debt {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "group_id")
    public long groupId;

    @ColumnInfo(name = "ammount")
    public double ammount;

    public Debt(long userId, long groupId, double ammount) {
        this.userId = userId;
        this.groupId = groupId;
        this.ammount = ammount;
    }
}
