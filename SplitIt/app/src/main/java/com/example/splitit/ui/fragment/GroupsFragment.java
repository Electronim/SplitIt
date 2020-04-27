package com.example.splitit.ui.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupWithFriends;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnGroupRepositoryActionListener;
import com.example.splitit.repository.OnRepositoryActionListener;
import com.example.splitit.ui.adapter.GroupAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment implements OnGroupRepositoryActionListener {
    private static final String TAG = "GroupsFragment";

    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;
    private FloatingActionButton mAddGroupFloatingButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupWithFriendsRepository = new GroupWithFriendsRepository(getContext());

        mGroupRecyclerView = view.findViewById(R.id.groups_recycler_view);
        mGroupRecyclerView.setHasFixedSize(true);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mGroupAdapter = new GroupAdapter(new ArrayList<>());
        mGroupRecyclerView.setAdapter(mGroupAdapter);

        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);

        mAddGroupFloatingButton = view.findViewById(R.id.fab_groups);
        mAddGroupFloatingButton.setOnClickListener(v -> {
            showAddGroupDialog();
        });
    }

    private void showAddGroupDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_group, null);

        final EditText groupNameEditText = dialogView.findViewById(R.id.edit_text_group_name);
        Button submitButton = dialogView.findViewById(R.id.button_submit);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);

        submitButton.setOnClickListener(v -> {
            // TODO: save 'em
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
        List<GroupWithFriends> groupWithFriends = mGroupAdapter.getmGroups();
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
}