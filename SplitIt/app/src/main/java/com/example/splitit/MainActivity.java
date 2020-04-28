package com.example.splitit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.Group;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.utils.BroadcastReceiverUtil;
import com.example.splitit.utils.RequestQueueHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnFriendRepositoryActionListener,
        OnActivityRepositoryActionListener {

    FriendRepository mFriendRepository;
    DebtRepository mDebtRepository;
    GroupRepository mGroupRepository;
    ActionRepository mActionRepository;

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_friends, R.id.navigation_groups, R.id.navigation_activity)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mFriendRepository = new FriendRepository(MainActivity.this);
        mDebtRepository = new DebtRepository(MainActivity.this);
        mGroupRepository = new GroupRepository(MainActivity.this);
        mActionRepository = new ActionRepository(MainActivity.this);

        getSharedPref();
    }

    private void getSharedPref(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstOpen = sharedPref.getBoolean(getString(R.string.app_open_first_time), true);
        if (firstOpen){
            showSyncRequestDialog();
            Log.i("SharedPref", "Application has been opened for the first time");
        }
    }

    private void showSyncRequestDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_request_backup, null);

        Button syncButton = dialogView.findViewById(R.id.button_sync);
        Button noSyncButton = dialogView.findViewById(R.id.button_no_sync);

        syncButton.setOnClickListener(v -> {
            syncDatabaseRequest();
            dialogBuilder.dismiss();
    });

       noSyncButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.app_open_first_time), false);
        editor.apply();
    }

    private void syncDatabaseRequest(){
        String url = "https://my-json-server.typicode.com/Electronim/SplitIt/backup";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    JSONArray friendList = response.getJSONArray("friends");
                    for (int index = 0; index < friendList.length(); index++) {
                        Friend friend = gson.fromJson(friendList.getJSONObject(index).toString(), Friend.class);
                        mFriendRepository.insertFriend(friend, MainActivity.this);
                        Log.d("GetRequest", friend.name + " " + friend.phoneNumber);
                    }

                    JSONArray debtList = response.getJSONArray("debts");
                    for (int index = 0; index < debtList.length(); index++) {
                        Debt debt = gson.fromJson(debtList.getJSONObject(index).toString(), Debt.class);
                        mDebtRepository.insertDebt(debt, MainActivity.this);
                        Log.d("GetRequest", debt.friendId + " " + debt.groupId + " " + debt.amount);
                    }

                    JSONArray groupList = response.getJSONArray("groups");
                    for (int index = 0; index < groupList.length(); index++) {
                        Group group = gson.fromJson(groupList.getJSONObject(index).toString(), Group.class);
                        mGroupRepository.insertGroup(group, MainActivity.this);
                        Log.d("GetRequest", group.name);
                    }

                    JSONArray actionList = response.getJSONArray("actions");
                    for (int index = 0; index < actionList.length(); index++) {
                        Action action = gson.fromJson(actionList.getJSONObject(index).toString(), Action.class);
                        mActionRepository.insertAction(action, MainActivity.this);
                        Log.d("GetRequest", action.message + " " + action.timestamp);
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        RequestQueueHelper.getRequestQueueHelperInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }

    @Override
    public void processFriendWithDebtsDBList(List<FriendWithDebts> friendWithDebtsList) {
    }

    @Override
    public void processActionList(List<Action> actionList) {

    }

}
