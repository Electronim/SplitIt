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
import com.example.splitit.model.Action;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.GroupWithFriendsRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.example.splitit.repository.OnRepositoryActionListener;
import com.example.splitit.ui.adapter.ActionAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment implements OnActivityRepositoryActionListener {
    private static final String TAG = "ActivityFragment";

    private RecyclerView mActionRecyclerView;
    private ActionAdapter mActiondapter;
    private ActionRepository mActionRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActionRepository = new ActionRepository(getContext());

        mActionRecyclerView = view.findViewById(R.id.activity_recycler_view);
        mActionRecyclerView.setHasFixedSize(true);
        mActionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActiondapter = new ActionAdapter(new ArrayList<Action>());
        mActionRecyclerView.setAdapter(mActiondapter);

        mActionRepository.getAllActions(ActivityFragment.this);
    }


    @Override
    public void notifyActionRecyclerView(List<Action> actionList) {
        List<Action> actions = mActiondapter.getmActions();
        actions.clear();
        actions.addAll(actionList);
        mActiondapter.notifyDataSetChanged();
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}