package com.example.splitit.model;

import androidx.room.Entity;

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

    public Debt(long id, long userId, long groupId, double ammount) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.ammount = ammount;
    }
}
