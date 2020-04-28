package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity
public class Debt implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "debt_id")
    public long id;

    @ColumnInfo(name = "friendId")
    public long friendId;

    @ColumnInfo(name = "groupId")
    public long groupId;

    @ColumnInfo(name = "amount")
    public double amount;

    public Debt(long friendId, long groupId, double amount) {
        this.friendId = friendId;
        this.groupId = groupId;
        this.amount = amount;
    }

    public JSONObject toJson() {
        JSONObject debtJson = new JSONObject();

        try {
            debtJson.put("friendId", friendId);
            debtJson.put("groupId", groupId);
            debtJson.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return debtJson;
    }
}
