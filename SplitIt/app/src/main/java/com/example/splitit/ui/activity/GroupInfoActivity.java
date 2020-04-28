package com.example.splitit.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.splitit.R;
import com.example.splitit.ui.fragment.GroupInfoFragment;

public class GroupInfoActivity extends AppCompatActivity {
    private static final String GROUP_INFO_FRAGMENT_LABEL = "GroupInfoFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        addGroupInfoFragment();
    }

    public void addGroupInfoFragment() {
        GroupInfoFragment fragment = new GroupInfoFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, GROUP_INFO_FRAGMENT_LABEL);
        fragmentTransaction.commit();
    }
}