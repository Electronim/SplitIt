package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Groups")
public class Group {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    public Group(String name) {
        this.name = name;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject groupJson = new JSONObject();

        groupJson.put("name", name);

        return groupJson;
    }

}
