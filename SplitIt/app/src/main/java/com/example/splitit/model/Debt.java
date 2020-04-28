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

    @ColumnInfo(name = "friend_debt_id")
    public long friendId;

    @ColumnInfo(name = "group_id")
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
            debtJson.put("friend_debt_id", friendId);
            debtJson.put("group_id", groupId);
            debtJson.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return debtJson;
    }
}
