package com.example.splitit.model.wrappers;
import com.example.splitit.model.FriendWithDebts;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendsWithDebtsWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<FriendWithDebts> mFriends;

    public FriendsWithDebtsWrapper(ArrayList<FriendWithDebts> friends) {
        this.mFriends = friends;
    }

    public ArrayList<FriendWithDebts> getFriends() {
        return mFriends;
    }
}
