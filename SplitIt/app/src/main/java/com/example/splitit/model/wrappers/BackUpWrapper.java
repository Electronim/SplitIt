package com.example.splitit.model.wrappers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BackUpWrapper {
    private List<JSONObject> friendList;
    private List<JSONObject> debtList;
    private List<JSONObject> groupList;
    private List<JSONObject> actionList;

    public BackUpWrapper(List<JSONObject> friendList, List<JSONObject> debtList, List<JSONObject> groupList, List<JSONObject> actionList) {
        this.friendList = friendList;
        this.debtList = debtList;
        this.groupList = groupList;
        this.actionList = actionList;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject backUpJson = new JSONObject();

        backUpJson.put("friends", friendList);
        backUpJson.put("debts", debtList);
        backUpJson.put("groups", groupList);
        backUpJson.put("actions", actionList);

        return backUpJson;
    }

    @Override
    public String toString() {
        return friendList.toString() + ", " + debtList.toString() + ", " + groupList.toString() +
                ", " + actionList.toString();
    }

    public List<JSONObject> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<JSONObject> friendList) {
        this.friendList = friendList;
    }

    public List<JSONObject> getDebtList() {
        return debtList;
    }

    public void setDebtList(List<JSONObject> debtList) {
        this.debtList = debtList;
    }

    public List<JSONObject> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<JSONObject> groupList) {
        this.groupList = groupList;
    }

    public List<JSONObject> getActionList() {
        return actionList;
    }

    public void setActionList(List<JSONObject> actionList) {
        this.actionList = actionList;
    }

}
