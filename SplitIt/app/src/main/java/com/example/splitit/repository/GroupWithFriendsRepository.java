package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupWithFriends;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

//    public void getGroupWithFriendsById(Long id, final OnGroupFriendRepositoryActionListener listener){
//        new GetGroupWithFriendsById(listener).execute(id);
//    }
//
//    private class GetGroupWithFriendsById extends AsyncTask<Long, Void, GroupWithFriends> {
//        OnGroupFriendRepositoryActionListener listener;
//
//        GetGroupWithFriendsById(OnGroupFriendRepositoryActionListener listener){
//            this.listener = listener;
//        }
//        @Override
//        protected GroupWithFriends doInBackground(Long ... ids) {
//            return appDatabase.groupWithFriendsDao().getGroupWithFriendsById(ids[0]);
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        protected void onPostExecute(GroupWithFriends groupWithFriends) {
//            super.onPostExecute(groupWithFriends);
//            listener.actionSuccess();
//
//            List<Long> resultList = groupWithFriends.friends.stream().map(gwf -> gwf.id).collect(Collectors.toList());
//            listener.notifyGroupFriendRecyclerView(resultList);
//        }
//    }
}
