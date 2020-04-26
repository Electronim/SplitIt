package com.example.splitit.repository;


import com.example.splitit.model.Action;

import java.util.List;

public interface OnActivityRepositoryActionListener extends OnRepositoryActionListener {
    void notifyActionRecyclerView(List<Action> actionList);
}
