package com.example.splitit.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.FriendWithDebts;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private ArrayList<FriendWithDebts> mFriends;

    public FriendAdapter(ArrayList<FriendWithDebts> mFriends) {
        this.mFriends = mFriends;
    }

    public ArrayList<FriendWithDebts> getFriends() {
        return mFriends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View friendView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_friend, parent, false);
        return new FriendViewHolder(friendView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendWithDebts friendWithDebts = mFriends.get(position);
        holder.friendName.setText(friendWithDebts.friend.name);
        Double amount = friendWithDebts.debts.stream().mapToDouble(p -> p.amount).sum();
        holder.friendAmount.setText(amount == 0 ? "settled up" : " owes you RON" + String.format("%.2f", amount));
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView friendName, friendAmount;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friendName = itemView.findViewById(R.id.textView_friend_name);
            this.friendAmount = itemView.findViewById(R.id.textView_amount);
        }
    }
}
