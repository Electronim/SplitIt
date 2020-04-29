package com.example.splitit.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<Action> mActions;

    public ActionAdapter(ArrayList<Action> mActions) {
        this.mActions = mActions;
    }

    public ArrayList<Action> getActions() {
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

        long millis = action.timestamp;

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMM").format(calendar.getTime());

        String timeStr = String.format("%02d:%02d", hour, minute);
        String dateStr = month + " " + day;

        holder.timeInfo.setText(timeStr);
        holder.dateInfo.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        public TextView actionMessage;
        public TextView timeInfo;
        public TextView dateInfo;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.actionMessage = itemView.findViewById(R.id.textView_actionMessage);
            this.timeInfo = itemView.findViewById(R.id.textView_time);
            this.dateInfo = itemView.findViewById(R.id.textView_date);
        }
    }
}
