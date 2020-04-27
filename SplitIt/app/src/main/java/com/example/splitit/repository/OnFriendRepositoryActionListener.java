package com.example.splitit.repository;

import com.example.splitit.model.FriendWithDebts;

import java.util.List;

public interface OnFriendRepositoryActionListener extends OnRepositoryActionListener{
    void notifyFriendRecyclerView(List<FriendWithDebts> friendWithDebtsList);
}
