package com.example.splitit.controller;

import android.app.Application;

import androidx.room.Room;

import com.example.splitit.dao.AppDatabase;

public class ApplicationController extends Application {
    private static ApplicationController mInstance;

    private static AppDatabase mAppDatabase;

    public static ApplicationController getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mAppDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "budget_management")
                .build();
    }

    public static AppDatabase getAppDatabase(){
        return mAppDatabase;
    }
}
