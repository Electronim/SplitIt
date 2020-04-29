package com.example.splitit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity
public class Debt implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "debtId")
    public long debtId;

    @ColumnInfo(name = "friendDebtId")
    public long friendDebtId;

    @ColumnInfo(name = "groupId")
    public long groupId;

    @ColumnInfo(name = "amount")
    public double amount;

    public Debt(long friendDebtId, long groupId, double amount) {
        this.friendDebtId = friendDebtId;
        this.groupId = groupId;
        this.amount = amount;
    }

    @Ignore
    public Debt(long debtId, long friendDebtId, long groupId, double amount) {
        this.debtId = debtId;
        this.friendDebtId = friendDebtId;
        this.groupId = groupId;
        this.amount = amount;
    }

    public JSONObject toJson() {
        JSONObject debtJson = new JSONObject();

        try {
            debtJson.put("debtId", debtId);
            debtJson.put("friendDebtId", friendDebtId);
            debtJson.put("groupId", groupId);
            debtJson.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return debtJson;
    }
}
