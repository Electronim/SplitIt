package com.example.splitit.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.splitit.R;
import com.example.splitit.model.Action;
import com.example.splitit.model.Contact;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Group;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.GroupRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityGeneratorUtil implements OnActivityRepositoryActionListener {
    private static final String TAG = "ActivityGeneratorUtil";

    private Context mContext;
    private ActionRepository mActionRepository;
    private GroupRepository mGroupRepository;
    private DebtRepository mDebtRepository;

    public ActivityGeneratorUtil(Context context) {
        this.mContext = context;
        this.mActionRepository = new ActionRepository(context);
        this.mGroupRepository = new GroupRepository(context);
        this.mDebtRepository = new DebtRepository(context);
    }

    private void generateAction(String message) {
        long timestamp = System.currentTimeMillis();
        Action action = new Action(message, timestamp);
        mActionRepository.insertAction(action, this);
    }

    public void generateCreatedGroupAction(Group group) {
        String groupName = group.name;
        String message = "The group `" + groupName + "` was created";
        generateAction(message);
    }

    public void generateDeletedGroupAction(Group group) {
        String groupName = group.name;
        String message = "The group `" + groupName + "` was deleted";
        generateAction(message);
    }

    public void generateContactAddedAsFriendAction(Contact contact) {
        String message = "The contact " + contact.name + " (" + contact.phoneNumber + ") was added as friend";
        generateAction(message);
    }

     public void generateAddedFriendToGroupAction(String friendName, String groupName) {
         String message = friendName + " was added to `" + groupName + "` group";
         generateAction(message);
     }

     public void generateDeletedFriendFromGroupAction(String friendName, String groupName) {
        String message = friendName + " was removed from `" + groupName + "` group";
        generateAction(message);
     }

    public void generateAddedExpenseAction(String friendName, String groupName, double amount) {
        String message = friendName + " owes you RON" + String.format("%.2f", amount) + " in `" + groupName + "` group!";
        generateAction(message);
    }

    public void generateSettledUpAction(String friendName, String groupName, double amount) {
        String message = "You settled up with " + friendName + " in `" + groupName + "` group (RON" + String.format("%.2f", amount) + ")";
        generateAction(message);
    }

    public void createSnackBar(View view, String message, List<Debt> debtList, Group group){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        View sView = snackbar.getView();
        TextView tv = (TextView) sView.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        snackbar.setAction("UNDO", new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                undoDeleteOperation(group, debtList);
            }
        });

        snackbar.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void undoDeleteOperation(Group group, List<Debt> debts) {
        mGroupRepository.insertGroup(group, this);
        debts.forEach(p -> mDebtRepository.insertDebt(p, this));

    }

    public void createSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        View sView = snackbar.getView();
        TextView tv = (TextView) sView.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        snackbar.show();
    }

    @Override
    public void processActionList(List<Action> actionList) {
//        for(Action action : actionList) {
//            Log.d(TAG, "processActionList: log: " + action.message);
//        }
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}
