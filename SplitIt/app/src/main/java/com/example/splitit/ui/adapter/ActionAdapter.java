package com.example.splitit.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(action.timestamp);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMM").format(calendar.getTime());
        String timeStr = String.format("%02d:%02d", hour, minute);
        String dateStr = month + " " + day;

        holder.timeInfo.setText(timeStr);
        holder.dateInfo.setText(dateStr);
        holder.iconLog.setImageResource(getResId(action.message));
    }

    public int getResId(String actionMessage) {
        if (actionMessage.contains("created")) {
            return R.drawable.ic_group_add_black_24dp;
        }

        if (actionMessage.contains("added")) {
            return R.drawable.ic_person_add_black_24dp;
        }

        if (actionMessage.contains("restored")) {
            return R.drawable.ic_history_black_24dp;
        }

        if (actionMessage.contains("removed") || actionMessage.contains("deleted")) {
            return R.drawable.ic_delete_black_24dp;
        }

        if (actionMessage.contains("owes")) {
            return R.drawable.ic_attach_money_black_24dp;
        }

        if (actionMessage.contains("settled")) {
            return R.drawable.ic_money_off_black_24dp;
        }

        return R.drawable.ic_info_outline_black_24dp;
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconLog;
        public TextView actionMessage;
        public TextView timeInfo;
        public TextView dateInfo;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iconLog = itemView.findViewById(R.id.image_view_icon_log);
            this.actionMessage = itemView.findViewById(R.id.textView_actionMessage);
            this.timeInfo = itemView.findViewById(R.id.textView_time);
            this.dateInfo = itemView.findViewById(R.id.textView_date);
        }
    }
}
