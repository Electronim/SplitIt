package com.example.splitit.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.wrappers.FriendsWithDebtsWrapper;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.FriendWithDebtsRepository;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.ui.activity.AddFriendActivity;
import com.example.splitit.ui.adapter.FriendAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment implements OnFriendRepositoryActionListener {
    private static final String TAG = "FriendsFragment";
    private static final int CONTACTS_PERMISSION_CODE = 100;
    public static final String FRIEND_LIST_EXTRA = "friend_list";

    private RecyclerView mFriendRecyclerView;
    private FriendAdapter mFriendAdapter;
    private FriendWithDebtsRepository mFriendWithDebtsRepository;
    private FriendRepository mFriendRepository;
    private DebtRepository mDebtRepository;
    private FloatingActionButton mAddFriendFloatingButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriendWithDebtsRepository = new FriendWithDebtsRepository(getContext());

        mFriendRecyclerView = view.findViewById(R.id.friends_recycler_view);
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFriendAdapter = new FriendAdapter(new ArrayList<>());
        mFriendRecyclerView.setAdapter(mFriendAdapter);

        mFriendWithDebtsRepository.getAllFriendsWithDebts(FriendsFragment.this);
        
        mAddFriendFloatingButton = view.findViewById(R.id.fab_friends);
        mAddFriendFloatingButton.setOnClickListener(v -> {
            // request permissions if they are not granted
            checkPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE);
        });
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                permission
        ) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[] { permission }, requestCode);
        } else {
            changeToAddFriendActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Read Contacts Permission Granted!",
                        Toast.LENGTH_SHORT)
                        .show();
                changeToAddFriendActivity();
            } else {
                Toast.makeText(getActivity(),
                        "Read Contacts Permission Denied!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void changeToAddFriendActivity() {
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        FriendsWithDebtsWrapper friendsWrapper = new FriendsWithDebtsWrapper(mFriendAdapter.getFriends());
        intent.putExtra(FRIEND_LIST_EXTRA, friendsWrapper);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processFriendWithDebtsDBList(List<FriendWithDebts> friendWithDebts) {
        List<FriendWithDebts> sortedList = friendWithDebts
                .stream()
                .sorted((obA, obB) -> obA.friend.name.compareTo(obB.friend.name))
                .collect(Collectors.toCollection(ArrayList::new));

        List<FriendWithDebts> friendWithDebtsList = mFriendAdapter.getFriends();
        friendWithDebtsList.clear();
        friendWithDebtsList.addAll(sortedList);
        mFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}