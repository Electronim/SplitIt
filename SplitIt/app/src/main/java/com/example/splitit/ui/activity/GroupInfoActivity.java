package com.example.splitit.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupFriendCrossRef;
import com.example.splitit.repository.FriendWithDebtsRepository;
import com.example.splitit.repository.GroupFriendCrossRefRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.repository.OnGroupFriendRepositoryActionListener;
import com.example.splitit.ui.adapter.GroupFriendAdapter;
import com.example.splitit.ui.fragment.GroupsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupInfoActivity extends AppCompatActivity implements
//        OnFriendRepositoryActionListener,
        OnGroupFriendRepositoryActionListener,
        GroupFriendAdapter.OnGroupFriendLongClickListener {
    private static final String TAG = "GroupInfoActivity";

    private RecyclerView mGroupFriendsRecyclerView;
    private GroupFriendAdapter mGroupFriendAdapter;

    private FloatingActionButton mAddGroupFloatingButton;
    private long mGroupId;

    private GroupRepository mGroupRepository;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;
    private GroupFriendCrossRefRepository mGroupFriendCrossRefRepository;
    private FriendWithDebtsRepository mFriendWithDebtsRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent intent = getIntent();
        mGroupId = intent.getLongExtra(GroupsFragment.USER_ID_EXTRA, -1);

        mGroupRepository = new GroupRepository(this);
        mGroupWithFriendsRepository = new GroupWithFriendsRepository(this);
        mGroupFriendCrossRefRepository = new GroupFriendCrossRefRepository(this);
        mFriendWithDebtsRepository = new FriendWithDebtsRepository(this);

        mGroupFriendsRecyclerView = findViewById(R.id.group_friends_recycler_view);
        mGroupFriendsRecyclerView.setHasFixedSize(true);
        mGroupFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: comment these!
        GroupFriendCrossRef groupFriend1 = new GroupFriendCrossRef(mGroupId, 1);
        GroupFriendCrossRef groupFriend2 = new GroupFriendCrossRef(mGroupId, 2);
        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend1, this);
        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend2, this);
        mGroupWithFriendsRepository.getGroupWithFriendsById(mGroupId, this);

        mGroupFriendAdapter = new GroupFriendAdapter(new ArrayList<>(), mGroupId, this);
        mGroupFriendsRecyclerView.setAdapter(mGroupFriendAdapter);

        mAddGroupFloatingButton = findViewById(R.id.fab_expenses);
        mAddGroupFloatingButton.setOnClickListener(v -> {
            showAddExpenseDialog();
        });
    }

    private void showAddExpenseDialog() {

    }

    @Override
    public void onGroupFriendLongClick(long friendId, long groupId) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void notifyGroupFriendRecyclerView(List<Long> groupFriendIds) {
        List<FriendWithDebts> friendWithDebts = mGroupFriendAdapter.getFriends();
        List<FriendWithDebts> filteredList =
                friendWithDebts
                        .stream()
                        .filter(fwb -> groupFriendIds.contains(fwb.friend.id)).
                        collect(Collectors.toList());
        friendWithDebts.clear();
        friendWithDebts.addAll(filteredList);
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void notifyFriendRecyclerView(List<FriendWithDebts> friendWithDebts) {
//        List<FriendWithDebts> sortedList = friendWithDebts
//                .stream()
//                .sorted((obA, obB) -> obA.friend.name.compareTo(obB.friend.name))
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        List<FriendWithDebts> friendWithDebtsList = mGroupFriendAdapter.getFriends();
//        friendWithDebtsList.clear();
//        friendWithDebtsList.addAll(sortedList);
//        mGroupFriendAdapter.notifyDataSetChanged();
//    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}
