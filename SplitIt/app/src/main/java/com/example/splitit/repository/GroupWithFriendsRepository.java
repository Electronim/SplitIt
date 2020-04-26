package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Entity;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupWithFriends;

import java.util.List;

@Entity
public class GroupWithFriendsRepository {
    private AppDatabase appDatabase;

    public GroupWithFriendsRepository(Context context){
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void getAllGroupWithFriends(final OnGroupRepositoryActionListener listener){
        new GetAllGroupWithFriends(listener).execute();
    }

    private class GetAllGroupWithFriends extends AsyncTask<Void, Void, List<GroupWithFriends>> {
        OnGroupRepositoryActionListener listener;

        GetAllGroupWithFriends(OnGroupRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<GroupWithFriends> doInBackground(Void ... voids) {
            return appDatabase.groupWithFriendsDao().getAllGroupsWithFriends();
        }

        @Override
        protected void onPostExecute(List<GroupWithFriends> groupWithFriendsList) {
            super.onPostExecute(groupWithFriendsList);
            listener.actionSuccess();
            listener.notifyGroupRecyclerView(groupWithFriendsList);
        }
    }

    public void getGroupWithFriends(final OnRepositoryActionListener listener){
        new GetGroupWithFriends(listener).execute();
    }

    private class GetGroupWithFriends extends AsyncTask<Long, Void, GroupWithFriends> {
        OnRepositoryActionListener listener;

        GetGroupWithFriends(OnRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected GroupWithFriends doInBackground(Long ... ids) {
            return appDatabase.groupWithFriendsDao().getGroupWithFriendsById(ids[0]);
        }

        @Override
        protected void onPostExecute(GroupWithFriends groupWithFriends) {
            super.onPostExecute(groupWithFriends);
            listener.actionSuccess();
        }
    }
}
