package com.example.splitit.repository;

import java.util.List;

public interface OnGroupFriendRepositoryActionListener extends OnRepositoryActionListener {
    void notifyGroupFriendRecyclerView(List<Long> groupFriendIds);
}
