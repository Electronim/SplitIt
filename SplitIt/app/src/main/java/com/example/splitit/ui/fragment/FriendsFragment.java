package com.example.splitit.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.MainActivity;
import com.example.splitit.R;
import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.Group;
import com.example.splitit.model.wrappers.BackUpWrapper;
import com.example.splitit.model.wrappers.FriendsWithDebtsWrapper;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.FriendWithDebtsRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.ui.activity.AddFriendActivity;
import com.example.splitit.ui.adapter.FriendAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment implements OnFriendRepositoryActionListener, OnActivityRepositoryActionListener {

    private static final String TAG = "FriendsFragment";
    private static final int CONTACTS_PERMISSION_CODE = 100;
    public static final String FRIEND_LIST_EXTRA = "friend_list";

    private RecyclerView mFriendRecyclerView;
    private FriendAdapter mFriendAdapter;
    private FriendWithDebtsRepository mFriendWithDebtsRepository;
    private FloatingActionButton mAddFriendFloatingButton;

    private FriendRepository mFriendRepository;
    private DebtRepository mDebtRepository;
    private GroupRepository mGroupRepository;
    private ActionRepository mActionRepository;

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
            backUpData();
        }

        return super.onOptionsItemSelected(item);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriendWithDebtsRepository = new FriendWithDebtsRepository(getContext());

        mActionRepository = new ActionRepository(getContext());
        mFriendRepository = new FriendRepository(getContext());
        mDebtRepository = new DebtRepository(getContext());
        mGroupRepository = new GroupRepository(getContext());

        mFriendRecyclerView = view.findViewById(R.id.friends_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFriendAdapter = new FriendAdapter(new ArrayList<>());
        mFriendRecyclerView.setAdapter(mFriendAdapter);

        mFriendWithDebtsRepository.getAllFriendsWithDebts(FriendsFragment.this);
        
        mAddFriendFloatingButton = view.findViewById(R.id.fab_friends);
        mAddFriendFloatingButton.setOnClickListener(v -> {
            // request permissions if they are not granted
            checkPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE);
        });

        mActionRepository.getAllActions(FriendsFragment.this);
        mFriendRepository.getAllFriends(FriendsFragment.this);
        mDebtRepository.getAllDebts(FriendsFragment.this);
        mGroupRepository.getAllGroups(FriendsFragment.this);
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                permission
        ) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[] { permission }, requestCode);
        } else {
            changeToAddFriendActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Read Contacts Permission Granted!",
                        Toast.LENGTH_SHORT)
                        .show();
                changeToAddFriendActivity();
            } else {
                Toast.makeText(getActivity(),
                        "Read Contacts Permission Denied!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void changeToAddFriendActivity() {
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        FriendsWithDebtsWrapper friendsWrapper = new FriendsWithDebtsWrapper(mFriendAdapter.getFriends());
        intent.putExtra(FRIEND_LIST_EXTRA, friendsWrapper);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void backUpData() {

        List<JSONObject> jsonFriends = friends.stream().map(Friend::toJson).collect(Collectors.toList());
        List<JSONObject> jsonDebts = debts.stream().map(Debt::toJson).collect(Collectors.toList());
        List<JSONObject> jsonGroups = groups.stream().map(Group::toJson).collect(Collectors.toList());
        List<JSONObject> jsonActions = actions.stream().map(Action::toJson).collect(Collectors.toList());
        BackUpWrapper backUpWrapper = new BackUpWrapper(jsonFriends, jsonDebts, jsonGroups, jsonActions);

        ((MainActivity)getActivity()).sendPostRequest(backUpWrapper);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processFriendWithDebtsDBList(List<FriendWithDebts> friendWithDebts) {
        List<FriendWithDebts> sortedList = friendWithDebts
                .stream()
                .sorted((obA, obB) -> obA.friend.name.compareTo(obB.friend.name))
                .collect(Collectors.toCollection(ArrayList::new));

        List<FriendWithDebts> friendWithDebtsList = mFriendAdapter.getFriends();
        friendWithDebtsList.clear();
        friendWithDebtsList.addAll(sortedList);
        mFriendAdapter.notifyDataSetChanged();
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
}