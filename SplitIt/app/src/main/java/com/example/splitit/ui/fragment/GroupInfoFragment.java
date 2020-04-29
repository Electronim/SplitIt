package com.example.splitit.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Debt;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupFriendCrossRef;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendWithDebtsRepository;
import com.example.splitit.repository.GroupFriendCrossRefRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.ui.activity.GroupInfoActivity;
import com.example.splitit.ui.adapter.GroupFriendAdapter;
import com.example.splitit.utils.ActivityGeneratorUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupInfoFragment extends Fragment implements
        OnFriendRepositoryActionListener,
        GroupFriendAdapter.OnGroupFriendLongClickListener {
    private static final String TAG = "GroupInfoActivity";

    private RecyclerView mGroupFriendsRecyclerView;
    private GroupFriendAdapter mGroupFriendAdapter;

    private Button mAddFriendToGroupButton;
    private FloatingActionButton mAddExpenseFloatingButton;
    private long mGroupId;
    private String mGroupName;

    private GroupRepository mGroupRepository;
    private DebtRepository mDebtRepository;
    private GroupWithFriendsRepository mGroupWithFriendsRepository;
    private GroupFriendCrossRefRepository mGroupFriendCrossRefRepository;
    private FriendWithDebtsRepository mFriendWithDebtsRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        mGroupId = intent.getLongExtra(GroupsFragment.GROUP_ID_EXTRA, -1);
        mGroupName = intent.getStringExtra(GroupsFragment.GROUP_NAME_EXTRA);

        getActivity().setTitle(mGroupName);
        return inflater.inflate(R.layout.fragment_group_info, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupRepository = new GroupRepository(getActivity());
        mDebtRepository = new DebtRepository(getActivity());
        mGroupWithFriendsRepository = new GroupWithFriendsRepository(getActivity());
        mGroupFriendCrossRefRepository = new GroupFriendCrossRefRepository(getActivity());
        mFriendWithDebtsRepository = new FriendWithDebtsRepository(getActivity());

        mGroupFriendsRecyclerView = view.findViewById(R.id.group_friends_recycler_view);
        mGroupFriendsRecyclerView.setHasFixedSize(true);
        mGroupFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mGroupFriendAdapter = new GroupFriendAdapter(new ArrayList<>(), mGroupId, this);
        mGroupFriendsRecyclerView.setAdapter(mGroupFriendAdapter);

        mAddExpenseFloatingButton = view.findViewById(R.id.fab_expenses);
        mAddExpenseFloatingButton.setOnClickListener(v -> {
            showAddExpenseDialog();
        });

        mAddFriendToGroupButton = view.findViewById(R.id.button_add_friend_to_group);
        mAddFriendToGroupButton.setOnClickListener(v -> {
            ArrayList<FriendWithDebts> friendWithDebts = mGroupFriendAdapter.getFriends();
            ((GroupInfoActivity) getActivity()).transactAddFriendToGroupFragment(friendWithDebts);
        });

        mFriendWithDebtsRepository.getAllFriendsWithDebts(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAddExpenseDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        final EditText expenseEditText = dialogView.findViewById(R.id.edit_text_expense);
        Button submitButton = dialogView.findViewById(R.id.button_submit_expense);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel_expense);

        submitButton.setOnClickListener(v -> {
            String expense = expenseEditText.getText().toString();
            double amount = 0;

            try {
                amount = Double.parseDouble(expense);
            } catch (NumberFormatException e) {
                amount = 0;
            }

            ArrayList<FriendWithDebts> friendWithDebts = getGroupFriends(mGroupFriendAdapter.getFriends());
            for(FriendWithDebts friend: friendWithDebts) {
                double amountPerFriend = amount / (friendWithDebts.size() + 1);

                Debt newDebt = new Debt(friend.friend.id, mGroupId, amountPerFriend);
                mDebtRepository.insertDebt(newDebt, this);

                ActivityGeneratorUtil util = new ActivityGeneratorUtil(getContext());
                util.generateAddedExpense(friend.friend.name, mGroupName, amountPerFriend);
            }

            mFriendWithDebtsRepository.getAllFriendsWithDebts(this);
            dialogBuilder.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onGroupFriendLongClick(long friendId, long groupId, View view) {
        List<FriendWithDebts> friendWithDebtsList = mGroupFriendAdapter.getFriends();
        String friendName = "";
        for(FriendWithDebts friend: friendWithDebtsList) {
            if (friend.friend.id != friendId) continue;

            for (Debt debt: friend.debts) {
                if (debt.groupId != groupId) continue;
                friendName = friend.friend.name;
                mDebtRepository.deleteDebt(debt.id, this);
            }
        }

        mGroupFriendCrossRefRepository.deleteGroupFriend(new GroupFriendCrossRef(groupId, friendId), this);
        mFriendWithDebtsRepository.getAllFriendsWithDebts(this);

        if (!TextUtils.isEmpty(friendName)) {
            ActivityGeneratorUtil util = new ActivityGeneratorUtil(getContext());
            util.generateDeletedFriendFromGroupAction(friendName, mGroupName);
            util.createSnackBar(view, friendName + " has been deleted from the group");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processFriendWithDebtsDBList(List<FriendWithDebts> friendWithDebts) {
        ArrayList<FriendWithDebts> filteredList = getGroupFriends(friendWithDebts);
        notifyRecyclerDataChange(filteredList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<FriendWithDebts> getGroupFriends(List<FriendWithDebts> friendWithDebts) {
        ArrayList<FriendWithDebts> filteredList =
                friendWithDebts
                        .stream()
                        .filter(fwd ->
                                fwd.debts
                                        .stream()
                                        .anyMatch(d -> d.groupId == mGroupId))
                        .collect(Collectors.toCollection(ArrayList::new));

        return filteredList;
    }

    private void notifyRecyclerDataChange(List<FriendWithDebts> filteredList) {
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
