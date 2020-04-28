package com.example.splitit.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Group;
import com.example.splitit.model.GroupWithFriends;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<GroupWithFriends> mGroups;
    private OnGroupListener mOnGroupListener;
    private OnLongClickGroupListener mOnLongClickGroupListener;

    public GroupAdapter(ArrayList<GroupWithFriends> mGroups,
                        OnGroupListener onGroupListener,
                        OnLongClickGroupListener onLongClickGroupListener) {
        this.mGroups = mGroups;
        this.mOnGroupListener = onGroupListener;
        this.mOnLongClickGroupListener = onLongClickGroupListener;
    }

    public ArrayList<GroupWithFriends> getGroups() {
        return mGroups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_group, parent, false);
        return new GroupViewHolder(view, mOnGroupListener, mOnLongClickGroupListener);
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

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView groupName;
        public TextView nrGroupParticipants;
        OnGroupListener onGroupListener;
        OnLongClickGroupListener onLongClickGroupListener;

        public GroupViewHolder(@NonNull View itemView,
                               OnGroupListener onGroupListener,
                               OnLongClickGroupListener onLongClickGroupListener) {
            super(itemView);
            this.groupName = itemView.findViewById(R.id.textView_groupName);
            this.nrGroupParticipants = itemView.findViewById(R.id.textView_nrGroupParticipants);
            this.onGroupListener = onGroupListener;
            this.onLongClickGroupListener = onLongClickGroupListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Group group = mGroups.get(getAdapterPosition()).group;
            onGroupListener.onGroupClick(group.id, group.name);
        }

        @Override
        public boolean onLongClick(View v) {
            long groupId = mGroups.get(getAdapterPosition()).group.id;
            onLongClickGroupListener.onGroupLongClick(groupId);
            return true;
        }
    }

    public interface OnGroupListener {
        void onGroupClick(long groupId, String groupName);
    }

    public interface OnLongClickGroupListener {
        void onGroupLongClick(long groupId);
    }
}
