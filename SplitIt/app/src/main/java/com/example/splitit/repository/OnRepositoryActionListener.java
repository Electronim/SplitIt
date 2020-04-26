package com.example.splitit.repository;

import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;

import java.util.List;

public interface OnRepositoryActionListener {
    void actionSuccess();
    void actionFailed(String message);
}
