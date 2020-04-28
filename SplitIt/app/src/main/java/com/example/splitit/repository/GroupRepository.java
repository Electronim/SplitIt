package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Friend;
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

    public void getAllGroups(final OnRepositoryActionListener listener){
        new GetAllGroups(listener).execute();
    }

    private class GetAllGroups extends AsyncTask<Void, Void, List<Group>> {
        OnRepositoryActionListener listener;

        GetAllGroups(OnRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<Group> doInBackground(Void ... voids) {
            return appDatabase.groupDao().getAllGroups();
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            super.onPostExecute(groups);
            listener.actionSuccess();
            listener.getAllGroups(groups);
        }
    }

    public void deleteGroup(final Long id, final OnRepositoryActionListener listener){
        new DeleteGroup(listener).execute(id);
    }

    private class DeleteGroup extends AsyncTask<Long, Void, List<Group>>{
        OnRepositoryActionListener listener;

        DeleteGroup(OnRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Group> doInBackground(Long... ids) {
            Group group = appDatabase.groupDao().getGroupById(ids[0]);
            appDatabase.groupDao().deleteGroup(group);
            return appDatabase.groupDao().getAllGroups();
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            super.onPostExecute(groups);
            listener.actionSuccess();
        }
    }
}
