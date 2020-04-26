package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Friend implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "friend_id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public Friend(String name, String phoneNumber){
       this.name = name;
       this.phoneNumber = phoneNumber;
    }
}
