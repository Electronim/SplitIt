package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Friend implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    // TODO: remove and leave only name!
    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public Friend(String firstName, String lastName, String phoneNumber){
       this.firstName = firstName;
       this.lastName = lastName;
       this.phoneNumber = phoneNumber;
    }
}
