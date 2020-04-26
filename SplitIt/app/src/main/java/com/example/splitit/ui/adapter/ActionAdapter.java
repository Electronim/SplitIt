package com.example.splitit.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Action;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<Action> mActions;

    public ActionAdapter(ArrayList<Action> mActions) {
        this.mActions = mActions;
    }

    public ArrayList<Action> getmActions() {
        return mActions;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View actionView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_action, parent, false);
        return new ActionViewHolder(actionView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Action action = mActions.get(position);
        holder.actionMessage.setText(action.message);
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        public TextView actionMessage;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.actionMessage = itemView.findViewById(R.id.textView_actionMessage);
        }
    }
}
