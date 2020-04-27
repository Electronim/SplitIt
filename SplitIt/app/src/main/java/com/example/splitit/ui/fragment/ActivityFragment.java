package com.example.splitit.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
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

    private RecyclerView mActionRecyclerView;
    private ActionAdapter mActionAdapter;
    private ActionRepository mActionRepository;
    private FriendRepository mFriendRepository;
    private GroupRepository mGroupRepository;
    private DebtRepository mDebtRepository;

    private Button backUpButton;

    private List<Friend> friends = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Debt> debts = new ArrayList<>();


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

        backUpButton = view.findViewById(R.id.backUp_button);
        backUpButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    backUpData();
                } catch (JSONException e) {
                    Log.i("REQUEST", "Failed to buckUp data");
                }
            }
        });

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

        sendPost(backUpWrapper);

    }

    public void sendPost(BackUpWrapper backUpWrapper) {
        String urlAdress = "https://my-json-server.typicode.com/Electronim/SplitIt/users";

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

                    JSONObject jsonParam = backUpWrapper.toJson();

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void notifyActionRecyclerView(List<Action> actionList) {
        List<Action> actions = mActionAdapter.getmActions();
        actions.clear();
        actions.addAll(actionList);
        mActionAdapter.notifyDataSetChanged();
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
}