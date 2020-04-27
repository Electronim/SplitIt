package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.GroupFriendCrossRef;

import java.util.List;

public class GroupFriendCrossRefRepository {
    private AppDatabase appDatabase;

    public GroupFriendCrossRefRepository(Context context) {
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertGroupFriend(final GroupFriendCrossRef groupFriend,
                                  final OnRepositoryActionListener listener) {
        new InsertGroupFriend(listener).execute(groupFriend);
    }

    private class InsertGroupFriend extends AsyncTask<GroupFriendCrossRef, Void, Void> {
        OnRepositoryActionListener listener;

        InsertGroupFriend(OnRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(GroupFriendCrossRef... groupFriendCrossRefs) {
            appDatabase.groupFriendCrossRefDao().insertGroupFriend(groupFriendCrossRefs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.actionSuccess();
        }
    }
}
