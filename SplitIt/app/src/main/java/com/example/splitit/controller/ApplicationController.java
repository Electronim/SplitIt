package com.example.splitit.controller;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.splitit.dao.AppDatabase;

public class ApplicationController extends Application {
    private static ApplicationController mInstance;
    private static Context mAppContext;
    private static AppDatabase mAppDatabase;

    public static ApplicationController getInstance(){
        return mInstance;
    }
    public static Context getAppContext() { return mAppContext; }

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mAppContext = getApplicationContext();
        mAppDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "budget_management")
                .build();
    }

    public static AppDatabase getAppDatabase(){
        return mAppDatabase;
    }
}
