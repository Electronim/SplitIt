package com.example.splitit.utils;

import android.content.Context;
import android.util.Log;

import com.example.splitit.model.Action;
import com.example.splitit.model.Contact;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Group;
import com.example.splitit.repository.ActionRepository;
import com.example.splitit.repository.OnActivityRepositoryActionListener;

import java.text.DecimalFormat;
import java.util.List;

public class ActivityGeneratorUtil implements OnActivityRepositoryActionListener {
    private static final String TAG = "ActivityGeneratorUtil";

    private Context mContext;
    private ActionRepository mActionRepository;

    public ActivityGeneratorUtil(Context context) {
        this.mContext = context;
        this.mActionRepository = new ActionRepository(context);
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
        String message = "The contact " + contact.name + "(" + contact.phoneNumber + ") was added as friend";
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

    public void generateAddedExpense(String friendName, String groupName, double amount) {
        String message = friendName + " owes you " + String.format("%.2f", amount) + "RON in `" + groupName + "` group!";
        generateAction(message);
    }

    @Override
    public void processActionList(List<Action> actionList) {
        for(Action action : actionList) {
            Log.d(TAG, "processActionList: log: " + action.message);
        }
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}
