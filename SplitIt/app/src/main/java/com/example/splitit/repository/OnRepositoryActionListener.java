package com.example.splitit.repository;

import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.Group;

import java.util.List;

public interface OnRepositoryActionListener {
    void actionSuccess();
    void actionFailed(String message);
    default void getAllFriends(List<Friend> friendsList){}
    default void getAllDebts(List<Debt> debtsList){}
    default void getAllGroups(List<Group> groupList){}
    default void getAllActions(List<Action> actionsList){}
}
