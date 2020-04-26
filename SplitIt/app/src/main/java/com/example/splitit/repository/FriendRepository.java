package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Friend;

import java.util.List;

public class FriendRepository {
    private AppDatabase appDatabase;

    public FriendRepository(Context context){
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertFriend(final Friend friend, final OnFriendRepositoryActionListener listener){
        new InsertFriend(listener).execute(friend);
    }

    private class InsertFriend extends AsyncTask<Friend, Void, List<Friend>> {
        OnFriendRepositoryActionListener listener;

        InsertFriend(OnFriendRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Friend> doInBackground(Friend... friends) {
            appDatabase.friendDao().insertFriend(friends[0]);
            return appDatabase.friendDao().getAllFriends();
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            listener.actionSuccess();
            listener.notifyRecyclerView(friends);
        }
    }

    public void getAllFriends(final OnFriendRepositoryActionListener listener){
        new GetAllFriends(listener).execute();
    }

    private class GetAllFriends extends AsyncTask<Void, Void, List<Friend>> {
        OnFriendRepositoryActionListener listener;

        GetAllFriends(OnFriendRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<Friend> doInBackground(Void ... voids) {
            return appDatabase.friendDao().getAllFriends();
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            listener.actionSuccess();
            listener.notifyRecyclerView(friends);
        }
    }

    public void deleteFriend(final Long id, final OnFriendRepositoryActionListener listener){
        new DeleteFriend(listener).execute(id);
    }

    private class DeleteFriend extends AsyncTask<Long, Void, List<Friend>>{
        OnFriendRepositoryActionListener listener;

        DeleteFriend(OnFriendRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Friend> doInBackground(Long... ids) {
            Friend friend = appDatabase.friendDao().getFriendById(ids[0]);
            appDatabase.friendDao().deleteFriend(friend);
            return appDatabase.friendDao().getAllFriends();
        }


        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            listener.actionSuccess();
            listener.notifyRecyclerView(friends);
        }
    }
}
