package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
public class Action {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "timestamp")
    public Timestamp timestamp;

    public Action(long id, String message, Timestamp timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }
}
