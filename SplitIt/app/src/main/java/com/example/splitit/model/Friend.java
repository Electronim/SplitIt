package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity
public class Friend implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "friendId")
    public long friendId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phoneNumber")
    public String phoneNumber;

    public Friend(String name, String phoneNumber){
       this.name = name;
       this.phoneNumber = phoneNumber;
    }

    @Ignore
    public Friend(long friendId, String name, String phoneNumber) {
        this.friendId = friendId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public JSONObject toJson(){
        JSONObject friendJson = new JSONObject();

        try {
            friendJson.put("friendId", friendId);
            friendJson.put("name", name);
            friendJson.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendJson;
    }
}
