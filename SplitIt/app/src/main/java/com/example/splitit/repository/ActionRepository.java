package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;

import java.util.List;

public class ActionRepository {
    private AppDatabase appDatabase;

    public ActionRepository(Context context){
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertAction(final Action action, final OnActivityRepositoryActionListener listener){
        new InsertAction(listener).execute(action);
    }

    private class InsertAction extends AsyncTask<Action, Void, List<Action>> {
        OnActivityRepositoryActionListener listener;

        InsertAction(OnActivityRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Action> doInBackground(Action... actions) {
            appDatabase.actionDao().insertAction(actions[0]);
            return appDatabase.actionDao().getAllActions();
        }

        @Override
        protected void onPostExecute(List<Action> actionList) {
            super.onPostExecute(actionList);
            listener.actionSuccess();
            listener.processActionList(actionList);
        }
    }

    public void getAllActions(final OnActivityRepositoryActionListener listener){
        new GetAllActions(listener).execute();
    }

    private class GetAllActions extends AsyncTask<Void, Void, List<Action>> {
        OnActivityRepositoryActionListener listener;

        GetAllActions(OnActivityRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<Action> doInBackground(Void ... voids) {
            return appDatabase.actionDao().getAllActions();
        }

        @Override
        protected void onPostExecute(List<Action> actionList) {
            super.onPostExecute(actionList);
            listener.actionSuccess();
            listener.processActionList(actionList);
            listener.getAllActions(actionList);
        }
    }

    public void deleteAction(final Long id, final OnActivityRepositoryActionListener listener){
        new ActionRepository.DeleteAction(listener).execute(id);
    }

    private class DeleteAction extends AsyncTask<Long, Void, List<Action>>{
        OnActivityRepositoryActionListener listener;

        DeleteAction(OnActivityRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Action> doInBackground(Long... ids) {
            Debt debt = appDatabase.debtDao().getDebtById(ids[0]);
            appDatabase.debtDao().deleteDebt(debt);
            return appDatabase.actionDao().getAllActions();
        }

        @Override
        protected void onPostExecute(List<Action> actionList) {
            super.onPostExecute(actionList);
            listener.actionSuccess();
            listener.processActionList(actionList);
        }
    }
}

