package com.example.splitit.model;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

//    public List<Integer> debts = new ArrayList<>();

    public Friend(long id, String firstName, String lastName, String phoneNumber){
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
       this.phoneNumber = phoneNumber;
    }
}