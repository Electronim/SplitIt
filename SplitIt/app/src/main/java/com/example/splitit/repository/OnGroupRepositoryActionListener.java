package com.example.splitit.repository;

import com.example.splitit.model.GroupWithFriends;

import java.util.List;

public interface OnGroupRepositoryActionListener extends OnRepositoryActionListener {
    void notifyGroupRecyclerView(List<GroupWithFriends> groupList);
}
