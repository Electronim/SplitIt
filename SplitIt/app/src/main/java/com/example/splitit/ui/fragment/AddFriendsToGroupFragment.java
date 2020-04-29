package com.example.splitit.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Contact;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.GroupFriendCrossRef;
import com.example.splitit.model.wrappers.FriendsWithDebtsWrapper;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.GroupFriendCrossRefRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnRepositoryActionListener;
import com.example.splitit.ui.activity.GroupInfoActivity;
import com.example.splitit.ui.adapter.ContactAdapter;
import com.example.splitit.utils.ActivityGeneratorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddFriendsToGroupFragment extends Fragment implements OnRepositoryActionListener {
    private static final String TAG = "AddFriendsToGroupFrag";

    private RecyclerView mFriendsRecyclerView;
    private RecyclerView.Adapter mContactAdapter;
    private Button mAddFriendsToGroupButton;

    private long mGroupId;
    private String mGroupName;

    private DebtRepository mDebtRepository;
    private FriendRepository mFriendRepository;
    private GroupFriendCrossRefRepository mGroupFriendCrossRefRepository;

    private List<Friend> friends = new ArrayList<>();
    private FriendsWithDebtsWrapper currentFriendsInGroup = new FriendsWithDebtsWrapper(new ArrayList<>());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentFriendsInGroup = (FriendsWithDebtsWrapper) getArguments()
                .getSerializable(GroupInfoActivity.FRIENDS_GROUP_EXTRA);

        return inflater.inflate(R.layout.fragment_add_friend_to_group, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        mGroupId = intent.getLongExtra(GroupsFragment.GROUP_ID_EXTRA, -1);
        mGroupName = intent.getStringExtra(GroupsFragment.GROUP_NAME_EXTRA);

        mDebtRepository = new DebtRepository(getContext());
        mFriendRepository = new FriendRepository(getContext());
        mGroupFriendCrossRefRepository = new GroupFriendCrossRefRepository(getActivity());

        mFriendsRecyclerView = view.findViewById(R.id.add_friends_recycler_view);
        mFriendsRecyclerView.setHasFixedSize(true);
        mFriendsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mContactAdapter = new ContactAdapter(new ArrayList<>(), getContext());
        mFriendsRecyclerView.setAdapter(mContactAdapter);

        mFriendRepository.getAllFriends(AddFriendsToGroupFragment.this);

        mAddFriendsToGroupButton = view.findViewById(R.id.button_add_friends_to_group);
        mAddFriendsToGroupButton.setOnClickListener(v -> {
            addFriendsToDatabase();
            ((GroupInfoActivity) getActivity()).transactGroupInfoFragment();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addFriendsToDatabase() {
        List<Contact> selected = ((ContactAdapter) mContactAdapter).getSelected();
        ActivityGeneratorUtil util = new ActivityGeneratorUtil(getContext());

        for (Contact contact: selected) {
            if (friends.stream().noneMatch(f -> f.name.equals(contact.name) &&
                    f.phoneNumber.equals(contact.phoneNumber))) continue;

            Friend friend = friends
                    .stream()
                    .filter(f -> f.name.equals(contact.name) &&
                            f.phoneNumber.equals(contact.phoneNumber))
                    .findFirst()
                    .get();

            Debt debt = new Debt(friend.id, mGroupId, 0);
            mDebtRepository.insertDebt(debt, this);

            GroupFriendCrossRef groupFriend = new GroupFriendCrossRef(mGroupId, friend.id);
            mGroupFriendCrossRefRepository.insertGroupFriend(groupFriend, this);

//            Toast.makeText(getActivity(), "The selected friends were added!", Toast.LENGTH_SHORT).show();
            util.generateAddedFriendToGroupAction(friend.name, mGroupName);
        }
    }

    private ArrayList <Contact> getFriendsToDisplay(List<Friend> friends) {
        ArrayList <Contact> contacts = new ArrayList<>();
        long id = 1;
        for(Friend friend: friends) {
            Contact contact = new Contact(String.valueOf(id++), friend.name, friend.phoneNumber);
            contacts.add(contact);
        }

        return contacts;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getAllFriends(List<Friend> friendsList) {
        friends.clear();
        friends.addAll(friendsList);
        ArrayList<Contact> listToBeDisplayed = getFriendsToDisplay(friendsList);

        for(FriendWithDebts fwd: currentFriendsInGroup.getFriends()) {
            Log.d(TAG, "getAllFriends: deja: " + fwd.friend.name);
        }

        List<Contact> sortedList = listToBeDisplayed
                .stream()
                .filter(c -> currentFriendsInGroup.getFriends()
                        .stream()
                        .noneMatch(cG -> c.name.equals(cG.friend.name) && c.phoneNumber.equals(cG.friend.phoneNumber))
                )
                .sorted((obA, obB) -> obA.name.compareTo(obB.name))
                .collect(Collectors.toList());

        List<Contact> adapterList = ((ContactAdapter) mContactAdapter).getContacts();
        adapterList.clear();
        adapterList.addAll(sortedList);
        mContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {
        
    }

    @Override
    public void actionFailed(String message) {

    }
}
