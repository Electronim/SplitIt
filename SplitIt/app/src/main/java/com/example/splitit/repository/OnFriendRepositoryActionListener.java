package com.example.splitit.repository;

import com.example.splitit.model.Friend;

import java.util.List;

public interface OnFriendRepositoryActionListener {

    void notifyRecyclerView(List<Friend> friends);
    void actionSuccess();
    void actionFailed(String message);
}
