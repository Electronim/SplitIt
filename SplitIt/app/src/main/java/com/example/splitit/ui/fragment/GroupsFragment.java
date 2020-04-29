package com.example.splitit.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.MainActivity;
import com.example.splitit.R;
import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.Group;
import com.example.splitit.model.GroupFriendCrossRef;
import com.example.splitit.model.GroupWithFriends;
import com.example.splitit.model.wrappers.BackUpWrapper;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.GroupFriendCrossRefRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.example.splitit.repository.OnGroupRepositoryActionListener;
import com.example.splitit.ui.activity.GroupInfoActivity;
import com.example.splitit.ui.adapter.GroupAdapter;
import com.example.splitit.utils.ActivityGeneratorUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GroupsFragment extends Fragment implements OnGroupRepositoryActionListener,
        GroupAdapter.OnGroupListener, GroupAdapter.OnLongClickGroupListener, OnActivityRepositoryActionListener {
    private static final String TAG = "GroupsFragment";
    public static final String GROUP_ID_EXTRA = "group_id";
    public static final String GROUP_NAME_EXTRA = "group_name";

    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private FloatingActionButton mAddGroupFloatingButton;

    private ActionRepository mActionRepository;
    private FriendRepository mFriendRepository;
    private DebtRepository mDebtRepository;
    private GroupRepository mGroupRepository;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;
    private GroupFriendCrossRefRepository mGroupFriendCrossRefRepository;

    private List<Friend> friends = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    List<Debt> allDebts = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActionRepository = new ActionRepository(getContext());
        mFriendRepository = new FriendRepository(getContext());
        mDebtRepository = new DebtRepository(getContext());
        mGroupRepository = new GroupRepository(getContext());
        mGroupWithFriendsRepository = new GroupWithFriendsRepository(getContext());
        mGroupFriendCrossRefRepository = new GroupFriendCrossRefRepository(getContext());

        mGroupRecyclerView = view.findViewById(R.id.groups_recycler_view);
        mGroupRecyclerView.setHasFixedSize(true);
        mGroupRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mDebtRepository.getAllDebts(GroupsFragment.this);

        mGroupAdapter = new GroupAdapter(new ArrayList<>(), this, this);
        mGroupRecyclerView.setAdapter(mGroupAdapter);

        mAddGroupFloatingButton = view.findViewById(R.id.fab_groups);
        mAddGroupFloatingButton.setOnClickListener(v -> {
            showAddGroupDialog(view);
        });

        mFriendRepository.getAllFriends(GroupsFragment.this);
        mActionRepository.getAllActions(GroupsFragment.this);
        mDebtRepository.getAllDebts(GroupsFragment.this);
        mGroupRepository.getAllGroups(GroupsFragment.this);
        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDebtRepository.getAllDebts(GroupsFragment.this);
        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
    }

    private void showAddGroupDialog(View view) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_group, null);

        final EditText groupNameEditText = dialogView.findViewById(R.id.edit_text_group_name);
        Button submitButton = dialogView.findViewById(R.id.button_submit);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);

        submitButton.setOnClickListener(v -> {
            String groupName = groupNameEditText.getText().toString();

            if (!TextUtils.isEmpty(groupName)) {
                Group newGroup = new Group(groupName);
                mGroupRepository.insertGroup(newGroup, GroupsFragment.this);

                ActivityGeneratorUtil util = new ActivityGeneratorUtil(getContext());
                util.generateCreatedGroupAction(newGroup);
                util.createSnackBar(view, "The group '" + groupName + "' has been added");
            }

            mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
            dialogBuilder.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void backUpData() {

        List<JSONObject> jsonFriends = friends.stream().map(Friend::toJson).collect(Collectors.toList());
        List<JSONObject> jsonDebts = allDebts.stream().map(Debt::toJson).collect(Collectors.toList());
        List<JSONObject> jsonGroups = groups.stream().map(Group::toJson).collect(Collectors.toList());
        List<JSONObject> jsonActions = actions.stream().map(Action::toJson).collect(Collectors.toList());
        BackUpWrapper backUpWrapper = new BackUpWrapper(jsonFriends, jsonDebts, jsonGroups, jsonActions);

        ((MainActivity)getActivity()).sendPostRequest(backUpWrapper);

    }

    @Override
    public void notifyGroupRecyclerView(List<GroupWithFriends> groupList) {
        List<GroupWithFriends> groupWithFriends = mGroupAdapter.getGroups();
        groupWithFriends.clear();
        groupWithFriends.addAll(groupList);
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAllDebts(List<Debt> debtsList) {
        allDebts.clear();
        allDebts.addAll(debtsList);
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

    @Override
    public void getAllFriends(List<Friend> friendsList) {
        friends.clear();
        friends.addAll(friendsList);
    }

    @Override
    public void onGroupClick(long groupId, String groupName) {
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra(GROUP_ID_EXTRA, groupId);
        intent.putExtra(GROUP_NAME_EXTRA, groupName);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGroupLongClick(long groupId, View view) {
        List<GroupWithFriends> groupWithFriends = mGroupAdapter.getGroups();

        String groupName = "";
        ArrayList<Debt> debts = new ArrayList<>();
        for (GroupWithFriends group: groupWithFriends) {
            if (group.group.id != groupId) continue;
            groupName = group.group.name;
            for (Friend friend: group.friends) {
                if (allDebts.stream().noneMatch(d -> d.groupId == groupId && d.friendId == friend.id)) {
                    continue;
                }

                // delete all friend's debts
                debts = allDebts
                        .stream()
                        .filter(d -> d.groupId == groupId && d.friendId == friend.id)
                        .collect(Collectors.toCollection(ArrayList::new));

                debts.forEach(d -> mDebtRepository.deleteDebt(d.id, this));

                // delete group ~ friend relation
                GroupFriendCrossRef groupFriend = new GroupFriendCrossRef(groupId, friend.id);
                mGroupFriendCrossRefRepository.deleteGroupFriend(groupFriend, this);
            }
        }

        if (!TextUtils.isEmpty(groupName)) {
            mGroupRepository.deleteGroup(groupId, this);

            ActivityGeneratorUtil util = new ActivityGeneratorUtil(getContext());
            util.generateDeletedGroupAction(new Group(groupName));
            Group group = new Group(groupName);
            group.setId(groupId);
            util.createSnackBar(view, "The group '" + groupName + "' has been deleted", debts, group);
//            if (undoDelete){
//                mGroupRepository.insertGroup(group, this);
//                debts.forEach(p -> mDebtRepository.insertDebt(p, this));
//            }

        }

        mGroupRepository.getAllGroups(this);
        mGroupWithFriendsRepository.getAllGroupWithFriends(this);
    }

    @Override
    public void processActionList(List<Action> actionList) {

    }
}