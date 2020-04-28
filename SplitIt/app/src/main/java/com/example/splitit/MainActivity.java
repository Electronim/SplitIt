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
import com.example.splitit.controller.ApplicationController;
import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.Group;
import com.example.splitit.model.wrappers.BackUpWrapper;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.example.splitit.repository.OnRepositoryActionListener;
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

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnActivityRepositoryActionListener {
    public static final int REQUEST_CODE=101;

    FriendRepository mFriendRepository;
    DebtRepository mDebtRepository;
    GroupRepository mGroupRepository;
    ActionRepository mActionRepository;

    NavController navController;

    private List<Friend> friends = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Debt> debts = new ArrayList<>();


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
            //navController.navigate(R.id.navigation_groups);
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
                navController.navigate(R.id.navigation_groups);
            }
        }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        RequestQueueHelper.getRequestQueueHelperInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void backUpData() throws JSONException {
//        Friend friend1 = new Friend("Dan", "079643241");
//        Friend friend2 = new Friend("Sandu", "0394543812");
//
//        Debt debt1 = new Debt(1, 1, 50);
//        Debt debt2 = new Debt(2,2,101.1);
//
//        Group group1 = new Group("Group1");
//        Group group2 = new Group("Group2");
//
//        Action action1 = new Action("action1", System.currentTimeMillis());
//        Action action2 = new Action("action2", System.currentTimeMillis());
//
//        List<JSONObject> friends = new ArrayList<>();
//        friends.add(friend1.toJson());
//        friends.add(friend2.toJson());
//
//        List<JSONObject> debts = new ArrayList<>();
//        debts.add(debt1.toJson());
//        debts.add(debt2.toJson());
//
//        List<JSONObject> groups = new ArrayList<>();
//        groups.add(group1.toJson());
//        groups.add(group2.toJson());
//
//        List<JSONObject> actions = new ArrayList<>();
//        actions.add(action1.toJson());
//        actions.add(action2.toJson())
//
//        BackUpWrapper backUpWrapper = new BackUpWrapper(friends, debts, groups, actions);

        List<JSONObject> jsonFriends = friends.stream().map(Friend::toJson).collect(Collectors.toList());
        List<JSONObject> jsonDebts = debts.stream().map(Debt::toJson).collect(Collectors.toList());
        List<JSONObject> jsonGroups = groups.stream().map(Group::toJson).collect(Collectors.toList());
        List<JSONObject> jsonActions = actions.stream().map(Action::toJson).collect(Collectors.toList());
        BackUpWrapper backUpWrapper = new BackUpWrapper(jsonFriends, jsonDebts, jsonGroups, jsonActions);

        sendPostRequest(backUpWrapper);

    }

    public void sendPostRequest(BackUpWrapper backUpWrapper) {
        String urlAdress = "https://my-json-server.typicode.com/Electronim/SplitIt/backup";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("backup", backUpWrapper.toJson());

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Thread.sleep(2000);
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    Context context = ApplicationController.getAppContext();
                    if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201)
                        sendNotification(context.getResources().getString(R.string.notif_title_backup_succeed),
                                context.getResources().getString(R.string.notif_message_backup_succeed));
                    else
                        sendNotification(context.getResources().getString(R.string.notif_title_backup_failed),
                                context.getResources().getString(R.string.notif_message_backup_failed));

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void sendNotification(String notifTitle, String notifMessage){
        Context context = ApplicationController.getAppContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, BroadcastReceiverUtil.class);
        notificationIntent.putExtra("notificationTitle", notifTitle);
        notificationIntent.putExtra("notificationMessage", notifMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 100, pendingIntent);
    }

    @Override
    public void getAllFriends(List<Friend> friendsList) {
        friends.addAll(friendsList);
    }

    @Override
    public void getAllDebts(List<Debt> debtsList) {
        debts.addAll(debtsList);
    }

    @Override
    public void getAllGroups(List<Group> groupList) {
        groups.addAll(groupList);
    }

    @Override
    public void getAllActions(List<Action> actionsList) {
        actions.addAll(actionsList);
    }

    @Override
    public void actionSuccess() {
    }

    @Override
    public void actionFailed(String message) {
    }

    @Override
    public void processActionList(List<Action> actionList) {

    }
}
