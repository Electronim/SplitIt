package com.example.splitit.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment implements OnGroupRepositoryActionListener {
    private static final String TAG = "GroupsFragment";

    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;

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

        mGroupAdapter = new GroupAdapter(new ArrayList<GroupWithFriends>());
        mGroupRecyclerView.setAdapter(mGroupAdapter);

        mGroupWithFriendsRepository.getAllGroupWithFriends(GroupsFragment.this);
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