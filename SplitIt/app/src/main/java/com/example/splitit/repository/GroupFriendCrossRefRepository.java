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

    public void deleteGroupFriend(final GroupFriendCrossRef groupFriendCrossRef,
                                  final OnRepositoryActionListener listener) {
        new DeleteGroupFriend(listener).execute(groupFriendCrossRef);
    }

    private class DeleteGroupFriend extends AsyncTask<GroupFriendCrossRef, Void, List<GroupFriendCrossRef>> {
        OnRepositoryActionListener listener;

        DeleteGroupFriend(OnRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<GroupFriendCrossRef> doInBackground(GroupFriendCrossRef... groupFriendCrossRefs) {
            GroupFriendCrossRef groupFriend = appDatabase.groupFriendCrossRefDao()
                    .getGroupFriendById(groupFriendCrossRefs[0].group_id, groupFriendCrossRefs[0].friend_id);
            appDatabase.groupFriendCrossRefDao().deleteGroupFriend(groupFriend);
            return null;
        }

        @Override
        protected void onPostExecute(List<GroupFriendCrossRef> groupFriendCrossRefs) {
            super.onPostExecute(groupFriendCrossRefs);
            listener.actionSuccess();
        }
    }
}
