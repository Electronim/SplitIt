package com.example.splitit.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupFriendCrossRef;
import com.example.splitit.repository.DebtRepository;
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
        OnFriendRepositoryActionListener,
//        OnGroupFriendRepositoryActionListener,
        GroupFriendAdapter.OnGroupFriendLongClickListener {
    private static final String TAG = "GroupInfoActivity";

    private RecyclerView mGroupFriendsRecyclerView;
    private GroupFriendAdapter mGroupFriendAdapter;

    private Button mAddFriendToGroupButton;
    private FloatingActionButton mAddGroupFloatingButton;
    private long mGroupId;

    private GroupRepository mGroupRepository;
    private DebtRepository mDebtRepository;
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
        mDebtRepository = new DebtRepository(this);
        mGroupWithFriendsRepository = new GroupWithFriendsRepository(this);
        mGroupFriendCrossRefRepository = new GroupFriendCrossRefRepository(this);
        mFriendWithDebtsRepository = new FriendWithDebtsRepository(this);

        mGroupFriendsRecyclerView = findViewById(R.id.group_friends_recycler_view);
        mGroupFriendsRecyclerView.setHasFixedSize(true);
        mGroupFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        // TODO: comment these!
//        GroupFriendCrossRef groupFriend1 = new GroupFriendCrossRef(mGroupId, 1);
//        GroupFriendCrossRef groupFriend2 = new GroupFriendCrossRef(mGroupId, 2);
//        GroupFriendCrossRef groupFriend3 = new GroupFriendCrossRef(mGroupId + 1, 3);
//        GroupFriendCrossRef groupFriend4 = new GroupFriendCrossRef(mGroupId + 1, 4);
//        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend1, this);
//        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend2, this);
//        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend3, this);
//        mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend4, this);

        mDebtRepository.insertDebt(new Debt(1, mGroupId, 0), this);
        mDebtRepository.insertDebt(new Debt(2, mGroupId, 0), this);
        mDebtRepository.insertDebt(new Debt(3, mGroupId + 1, 0), this);
        mDebtRepository.insertDebt(new Debt(4, mGroupId + 1, 0), this);

        mGroupFriendAdapter = new GroupFriendAdapter(new ArrayList<>(), mGroupId, this);
        mGroupFriendsRecyclerView.setAdapter(mGroupFriendAdapter);

        mAddGroupFloatingButton = findViewById(R.id.fab_expenses);
        mAddGroupFloatingButton.setOnClickListener(v -> {
            showAddExpenseDialog();
        });

        mAddFriendToGroupButton = findViewById(R.id.button_add_friend_to_group);

        mFriendWithDebtsRepository.getAllFriendsWithDebts(this);
    }

    private void showAddExpenseDialog() {

    }

    @Override
    public void onGroupFriendLongClick(long friendId, long groupId) {
        List<FriendWithDebts> friendWithDebtsList = mGroupFriendAdapter.getFriends();
        for(FriendWithDebts friend: friendWithDebtsList) {
            if (friend.friend.id != friendId) continue;

            for (Debt debt: friend.debts) {
                if (debt.groupId != groupId) continue;
                mDebtRepository.deleteDebt(debt.id, this);
            }
        }

        mGroupFriendCrossRefRepository.deleteGroupFriend(new GroupFriendCrossRef(groupId, friendId), this);
        mFriendWithDebtsRepository.getAllFriendsWithDebts(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processFriendWithDebtsDBList(List<FriendWithDebts> friendWithDebts) {
        ArrayList<FriendWithDebts> filteredList =
                friendWithDebts
                        .stream()
                        .filter(fwd ->
                                fwd.debts
                                .stream()
                                .anyMatch(d -> d.groupId == mGroupId))
                        .collect(Collectors.toCollection(ArrayList::new));

        List<FriendWithDebts> friendWithDebtsList = mGroupFriendAdapter.getFriends();
        friendWithDebtsList.clear();
        friendWithDebtsList.addAll(filteredList);
        mGroupFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}
