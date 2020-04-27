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
    private OnGroupListener mOnGroupListener;

    public GroupAdapter(ArrayList<GroupWithFriends> mGroups, OnGroupListener onGroupListener) {
        this.mGroups = mGroups;
        this.mOnGroupListener = onGroupListener;
    }

    public ArrayList<GroupWithFriends> getGroups() {
        return mGroups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_group, parent, false);
        return new GroupViewHolder(view, mOnGroupListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupWithFriends groupWithFriends = mGroups.get(position);
        holder.groupName.setText(groupWithFriends.group.name);
        holder.nrGroupParticipants.setText(String.valueOf(groupWithFriends.friends.size()));
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groupName;
        public TextView nrGroupParticipants;
        OnGroupListener onGroupListener;

        public GroupViewHolder(@NonNull View itemView, OnGroupListener onGroupListener) {
            super(itemView);
            this.groupName = itemView.findViewById(R.id.textView_groupName);
            this.nrGroupParticipants = itemView.findViewById(R.id.textView_nrGroupParticipants);
            this.onGroupListener = onGroupListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long groupId = mGroups.get(getAdapterPosition()).group.id;
            onGroupListener.onGroupClick(groupId);
        }
    }

    public interface OnGroupListener {
        void onGroupClick(long groupId);
    }
}
