package com.example.splitit.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.splitit.R;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.wrappers.FriendsWithDebtsWrapper;
import com.example.splitit.ui.fragment.AddFriendsToGroupFragment;
import com.example.splitit.ui.fragment.GroupInfoFragment;

import java.util.ArrayList;

public class GroupInfoActivity extends AppCompatActivity {
    private static final String GROUP_INFO_FRAGMENT_LABEL = "GroupInfoFragment";
    public static final String FRIENDS_GROUP_EXTRA = "friends_in_group";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        transactGroupInfoFragment();
    }

    public void transactGroupInfoFragment() {
        GroupInfoFragment fragment = new GroupInfoFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, GROUP_INFO_FRAGMENT_LABEL);
        fragmentTransaction.commit();
    }

    public void transactAddFriendToGroupFragment(ArrayList<FriendWithDebts> friends) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FRIENDS_GROUP_EXTRA, new FriendsWithDebtsWrapper(friends));

        AddFriendsToGroupFragment fragment = new AddFriendsToGroupFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, GROUP_INFO_FRAGMENT_LABEL);
        fragmentTransaction.commit();
    }
}