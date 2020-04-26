package com.example.splitit.ui.friends;

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
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.FriendWithDebtsRepository;
import com.example.splitit.repository.OnFriendRepositoryActionListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements OnFriendRepositoryActionListener {
    private static final String TAG = "FriendsFragment";

    private RecyclerView mFriendRecyclerView;
    private FriendAdapter mFriendAdapter;
    private FriendWithDebtsRepository mFriendWithDebtRepository;
    private FriendRepository mFriendRepository;
    private DebtRepository mDebtRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriendWithDebtRepository = new FriendWithDebtsRepository(getContext());

        // TODO: Remove these comments after we are able to insert Friends from Contacts;
//        mFriendRepository = new FriendRepository(getContext());
//        mDebtRepository = new DebtRepository(getContext());
//
//        Friend friend1 = new Friend(1,"Dan", "Darii", "079610248");
//        Friend friend2 = new Friend(2,"Vlad", "Darii", "079610248");
//
//        Debt debt1 = new Debt(1, 1, 1, 55.5);
//        Debt debt2 = new Debt(2, 1, 1, 55.5);
//        Debt debt3 = new Debt(3, 2, 1, 55.5);
//
//        mFriendRepository.insertFriend(friend1, FriendsFragment.this);
//        mFriendRepository.insertFriend(friend2, FriendsFragment.this);
//        mDebtRepository.insertDebt(debt1, FriendsFragment.this);
//        mDebtRepository.insertDebt(debt2, FriendsFragment.this);
//        mDebtRepository.insertDebt(debt3, FriendsFragment.this);

        mFriendRecyclerView = view.findViewById(R.id.friends_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFriendAdapter = new FriendAdapter(new ArrayList<FriendWithDebts>());
        mFriendRecyclerView.setAdapter(mFriendAdapter);

        mFriendWithDebtRepository.getAllFriendsWithDebts(FriendsFragment.this);
    }

    @Override
    public void notifyRecyclerView(List<FriendWithDebts> friendsWithDebts) {
        List<FriendWithDebts> friendWithDebtsList = mFriendAdapter.getmFriends();
        friendWithDebtsList.clear();
        friendWithDebtsList.addAll(friendsWithDebts);
        mFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}