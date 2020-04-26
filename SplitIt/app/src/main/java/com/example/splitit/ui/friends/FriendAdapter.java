package com.example.splitit.ui.friends;

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

    public ArrayList<FriendWithDebts> getmFriends() {
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
        holder.friendFirstName.setText(friendWithDebts.friend.firstName);
        holder.friendLastName.setText(friendWithDebts.friend.lastName);
        final long friendId = friendWithDebts.friend.id;
        Double ammount = friendWithDebts.detbs.stream().filter(p -> p.userId == friendId).mapToDouble(p -> p.ammount).sum();
        holder.friendAmmount.setText(ammount.toString());
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView friendFirstName, friendLastName, friendAmmount;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friendFirstName = itemView.findViewById(R.id.textView_firstName);
            this.friendLastName = itemView.findViewById(R.id.textView_lastName);
            this.friendAmmount = itemView.findViewById(R.id.textView_ammount);
        }
    }
}
