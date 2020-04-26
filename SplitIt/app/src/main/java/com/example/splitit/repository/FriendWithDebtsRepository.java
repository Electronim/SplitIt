package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;

import java.util.List;

public class FriendWithDebtsRepository {
    private AppDatabase appDatabase;

    public FriendWithDebtsRepository(Context context){
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void getAllFriendsWithDebts(final OnFriendRepositoryActionListener listener){
        new GetAllFriendsWithDebts(listener).execute();
    }

    private class GetAllFriendsWithDebts extends AsyncTask<Void, Void, List<FriendWithDebts>> {
        OnFriendRepositoryActionListener listener;

        GetAllFriendsWithDebts(OnFriendRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<FriendWithDebts> doInBackground(Void ... voids) {
            return appDatabase.friendWithDebtsDao().getAllFriendsWithDebts();
        }

        @Override
        protected void onPostExecute(List<FriendWithDebts> friendsWithDebts) {
            super.onPostExecute(friendsWithDebts);
            listener.actionSuccess();
            listener.notifyRecyclerView(friendsWithDebts);
        }
    }
}
