package com.example.splitit.ui.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.MainActivity;
import com.example.splitit.R;
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
import com.example.splitit.ui.adapter.ActionAdapter;
import com.example.splitit.utils.BroadcastReceiverUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityFragment extends Fragment implements OnActivityRepositoryActionListener {
    private static final String TAG = "ActivityFragment";
    public static final int REQUEST_CODE=101;

    private RecyclerView mActionRecyclerView;
    private ActionAdapter mActionAdapter;

    private ActionRepository mActionRepository;
    private FriendRepository mFriendRepository;
    private GroupRepository mGroupRepository;
    private DebtRepository mDebtRepository;

    private List<Friend> friends = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Debt> debts = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.back_up_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_up_button) {
            try {
                backUpData();
            } catch (JSONException e) {
                Log.i("REQUEST", "Failed to back up data");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActionRepository = new ActionRepository(getContext());
        mFriendRepository = new FriendRepository(getContext());
        mDebtRepository = new DebtRepository(getContext());
        mGroupRepository = new GroupRepository(getContext());

        mActionRecyclerView = view.findViewById(R.id.activity_recycler_view);
        mActionRecyclerView.setHasFixedSize(true);
        mActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActionAdapter = new ActionAdapter(new ArrayList<Action>());
        mActionRecyclerView.setAdapter(mActionAdapter);

        mActionRepository.getAllActions(ActivityFragment.this);
        mFriendRepository.getAllFriends(ActivityFragment.this);
        mDebtRepository.getAllDebts(ActivityFragment.this);
        mGroupRepository.getAllGroups(ActivityFragment.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void backUpData() throws JSONException {
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

        ((MainActivity)getActivity()).sendPostRequest(backUpWrapper);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processActionList(List<Action> actionList) {
        List<Action> actions = mActionAdapter.getActions();
        List<Action> sortedList = actionList
                .stream()
                .sorted((obA, obB) -> (int) obB.timestamp - (int) obA.timestamp)
                .collect(Collectors.toList());

        actions.clear();
        actions.addAll(sortedList);
        mActionAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAllFriends(List<Friend> friendsList) {
        friends.clear();
        friends.addAll(friendsList);
    }

    @Override
    public void getAllDebts(List<Debt> debtsList) {
        debts.clear();
        debts.addAll(debtsList);
    }

    @Override
    public void getAllGroups(List<Group> groupList) {
        groups.clear();
        groups.addAll(groupList);
    }

    @Override
    public void getAllActions(List<Action> actionsList) {
        actions.clear();
        actions.addAll(actionsList);
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}