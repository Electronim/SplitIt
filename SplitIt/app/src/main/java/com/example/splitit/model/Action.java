package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

@Entity
public class Action {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "action_id")
    public long id;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    public Action(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject actionJson = new JSONObject();

        actionJson.put("message", message);
        actionJson.put("timestamp", timestamp);

        return actionJson;
    }
}
