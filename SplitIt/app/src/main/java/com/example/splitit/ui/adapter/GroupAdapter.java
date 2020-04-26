package com.example.splitit.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.GroupWithFriends;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<GroupWithFriends> mGroups;

    public GroupAdapter(ArrayList<GroupWithFriends> mGroups) {
        this.mGroups = mGroups;
    }

    public ArrayList<GroupWithFriends> getmGroups() {
        return mGroups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_group, parent, false);
        return new GroupViewHolder(groupView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupWithFriends groupWithFriends = mGroups.get(position);
        holder.groupName.setText(groupWithFriends.group.name);
        holder.nrGroupParticipants.setText(groupWithFriends.friends.size());
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName, nrGroupParticipants;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            this.groupName = itemView.findViewById(R.id.textView_groupName);
            this.nrGroupParticipants = itemView.findViewById(R.id.textView_nrGroupParticipants);
        }
    }
}
