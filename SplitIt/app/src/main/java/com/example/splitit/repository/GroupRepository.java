package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Group;

import java.util.List;

public class GroupRepository {
    private AppDatabase appDatabase;

    public GroupRepository(Context context) {
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertGroup(final Group group, final OnRepositoryActionListener listener) {
        new InsertGroup(listener).execute(group);
    }

    private class InsertGroup extends AsyncTask<Group, Void, List<Group>> {
        OnRepositoryActionListener listener;

        InsertGroup(OnRepositoryActionListener listener) { this.listener = listener; }

        @Override
        protected List<Group> doInBackground(Group... groups) {
            appDatabase.groupDao().insertGroup(groups[0]);
            return appDatabase.groupDao().getAllGroups();
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            super.onPostExecute(groups);
            listener.actionSuccess();
        }
    }
}
