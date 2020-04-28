package com.example.splitit.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Group;
import com.example.splitit.model.GroupWithFriends;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnGroupRepositoryActionListener;
import com.example.splitit.ui.activity.GroupInfoActivity;
import com.example.splitit.ui.adapter.GroupAdapter;
import com.example.splitit.utils.ActivityGeneratorUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment implements OnGroupRepositoryActionListener, GroupAdapter.OnGroupListener {
    private static final String TAG = "GroupsFragment";
    public static final String GROUP_ID_EXTRA = "group_id";
    public static final String GROUP_NAME_EXTRA = "group_name";

    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private FloatingActionButton mAddGroupFloatingButton;

    private GroupRepository mGroupRepository;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupRepository = new GroupRepository(getContext());
        mGroupWithFriendsRepository = new GroupWithFriendsRepository(getContext());

        mGroupRecyclerView = view.findViewById(R.id.groups_recycler_view);
        mGroupRecyclerView.setHasFixedSize(true);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mGroupAdapter = new GroupAdapter(new ArrayList<>(), this);
        mGroupRecyclerView.setAdapter(mGroupAdapter);

        mAddGroupFloatingButton = view.findViewById(R.id.fab_groups);
        mAddGroupFloatingButton.setOnClickListener(v -> {
            showAddGroupDialog();
        });

        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
    }

    private void showAddGroupDialog() {
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

    @Override
    public void notifyGroupRecyclerView(List<GroupWithFriends> groupList) {
        List<GroupWithFriends> groupWithFriends = mGroupAdapter.getGroups();
        groupWithFriends.clear();
        groupWithFriends.addAll(groupList);
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }

    @Override
    public void onGroupClick(long groupId, String groupName) {
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra(GROUP_ID_EXTRA, groupId);
        intent.putExtra(GROUP_NAME_EXTRA, groupName);
        startActivity(intent);
    }
}