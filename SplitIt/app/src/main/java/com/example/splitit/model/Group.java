package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Groups")
public class Group {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "groupId")
    public long groupId;

    @ColumnInfo(name = "name")
    public String name;

    public Group(String name) {
        this.name = name;
    }

    @Ignore
    public Group(long groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public JSONObject toJson() {
        JSONObject groupJson = new JSONObject();

        try {
            groupJson.put("groupId", groupId);
            groupJson.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupJson;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
