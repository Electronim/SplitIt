package com.example.splitit.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.splitit.controller.ApplicationController;
import com.example.splitit.dao.AppDatabase;
import com.example.splitit.model.Debt;

import java.util.List;

public class DebtRepository {
    private AppDatabase appDatabase;

    public DebtRepository(Context context){
        this.appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertDebt(final Debt debt, final OnRepositoryActionListener listener){
        new DebtRepository.InsertDebt(listener).execute(debt);
    }

    private class InsertDebt extends AsyncTask<Debt, Void, List<Debt>> {
        OnRepositoryActionListener listener;

        InsertDebt(OnRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Debt> doInBackground(Debt... debts) {
            appDatabase.debtDao().insertDebt(debts[0]);
            return appDatabase.debtDao().getAllDebts();
        }

        @Override
        protected void onPostExecute(List<Debt> debts) {
            super.onPostExecute(debts);
            listener.actionSuccess();
        }
    }

    public void getAllDebts(final OnRepositoryActionListener listener){
        new GetAllDebts(listener).execute();
    }

    private class GetAllDebts extends AsyncTask<Void, Void, List<Debt>> {
        OnRepositoryActionListener listener;

        GetAllDebts(OnRepositoryActionListener listener){
            this.listener = listener;
        }
        @Override
        protected List<Debt> doInBackground(Void ... voids) {
            return appDatabase.debtDao().getAllDebts();
        }

        @Override
        protected void onPostExecute(List<Debt> debts) {
            super.onPostExecute(debts);
            listener.actionSuccess();
        }
    }

    public void deleteDebt(final Long id, final OnRepositoryActionListener listener){
        new DeleteDebt(listener).execute(id);
    }

    private class DeleteDebt extends AsyncTask<Long, Void, List<Debt>>{
        OnRepositoryActionListener listener;

        DeleteDebt(OnRepositoryActionListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Debt> doInBackground(Long... ids) {
            Debt debt = appDatabase.debtDao().getDebtById(ids[0]);
            appDatabase.debtDao().deleteDebt(debt);
            return appDatabase.debtDao().getAllDebts();
        }

        @Override
        protected void onPostExecute(List<Debt> debts) {
            super.onPostExecute(debts);
            listener.actionSuccess();
        }
    }
}
