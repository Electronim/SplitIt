package com.example.splitit.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.FriendWithDebts;

import java.util.ArrayList;

public class GroupFriendAdapter extends RecyclerView.Adapter<GroupFriendAdapter.ViewHolder> {
    private ArrayList<FriendWithDebts> mFriends;
    private long mGroupId;
    private OnItemsClickListener mOnItemsClickListener;
    private OnGroupFriendLongClickListener mOnGroupFriendLongClickListener;

    public ArrayList<FriendWithDebts> getFriends() {
        return mFriends;
    }

    public GroupFriendAdapter(ArrayList<FriendWithDebts> mFriends, long mGroupId,
                              OnGroupFriendLongClickListener onGroupFriendLongClickListener,
                              OnItemsClickListener onItemsClickListener) {
        this.mFriends = mFriends;
        this.mGroupId = mGroupId;
        this.mOnGroupFriendLongClickListener = onGroupFriendLongClickListener;
        this.mOnItemsClickListener = onItemsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.view_group_friend, parent, false);
        return new ViewHolder(view, mOnGroupFriendLongClickListener, mOnItemsClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendWithDebts friendWithDebts = mFriends.get(position);
        holder.friendName.setText(friendWithDebts.friend.name);

        Double amount = friendWithDebts.debts.stream()
                .filter(fwd -> fwd.groupId == mGroupId)
                .mapToDouble(d -> d.amount)
                .sum();
        holder.friendAmount.setText(amount == 0 ? "settled up" : " owes you RON" + String.format("%.2f", amount));

        holder.settleUp.setOnClickListener(v -> {
            long friendId = friendWithDebts.friend.friendId;
            holder.onItemsClickListener.onClick(friendId, mGroupId, v);
        });

        holder.addExpense.setOnClickListener(v -> {
            long friendId = friendWithDebts.friend.friendId;
            String friendName = friendWithDebts.friend.name;
            holder.onItemsClickListener.onAddExpenseClick(friendId, friendName);
        });
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView friendName;
        public TextView friendAmount;
        public ImageView settleUp;
        public ImageView addExpense;
        public OnItemsClickListener onItemsClickListener;
        public OnGroupFriendLongClickListener onGroupFriendLongClickListener;

        public ViewHolder(@NonNull View itemView, OnGroupFriendLongClickListener onGroupFriendLongClickListener, OnItemsClickListener onItemsClickListener) {
            super(itemView);
            this.friendName = itemView.findViewById(R.id.textView_friend_group_name);
            this.friendAmount = itemView.findViewById(R.id.textView_friend_group_amount);
            this.settleUp = itemView.findViewById(R.id.image_view_settle_up);
            this.addExpense = itemView.findViewById(R.id.image_view_add_expense_friend);

            this.onItemsClickListener = onItemsClickListener;
            this.onGroupFriendLongClickListener = onGroupFriendLongClickListener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            long friendId = mFriends.get(getAdapterPosition()).friend.friendId;
            onGroupFriendLongClickListener.onGroupFriendLongClick(friendId, mGroupId, v);
            return true;
        }
    }

    public interface OnItemsClickListener {
        void onClick(long friendId, long groupId, View v);
        void onAddExpenseClick(long friendId, String friendName);
    }

    public interface OnGroupFriendLongClickListener {
        void onGroupFriendLongClick(long friendId, long groupId, View v);
    }
}
