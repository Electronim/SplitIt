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
import android.widget.Button;
import android.widget.Toast;

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

    private RecyclerView mActionRecyclerView;
    private ActionAdapter mActionAdapter;
    private ActionRepository mActionRepository;

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
                ((MainActivity) getActivity()).backUpData();
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

        mActionRecyclerView = view.findViewById(R.id.activity_recycler_view);
        mActionRecyclerView.setHasFixedSize(true);
        mActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActionAdapter = new ActionAdapter(new ArrayList<Action>());
        mActionRecyclerView.setAdapter(mActionAdapter);

        mActionRepository.getAllActions(ActivityFragment.this);
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
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}