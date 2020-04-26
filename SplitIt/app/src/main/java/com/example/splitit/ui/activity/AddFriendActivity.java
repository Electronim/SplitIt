package com.example.splitit.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.example.splitit.MainActivity;
import com.example.splitit.R;
import com.example.splitit.model.Contact;
import com.example.splitit.model.Debt;
import com.example.splitit.model.Friend;
import com.example.splitit.model.FriendWithDebts;
import com.example.splitit.model.wrappers.FriendsWithDebtsWrapper;
import com.example.splitit.repository.DebtRepository;
import com.example.splitit.repository.FriendRepository;
import com.example.splitit.repository.OnFriendRepositoryActionListener;
import com.example.splitit.repository.OnRepositoryActionListener;
import com.example.splitit.ui.adapter.ContactAdapter;
import com.example.splitit.ui.adapter.FriendAdapter;
import com.example.splitit.ui.fragment.FriendsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddFriendActivity extends AppCompatActivity implements OnRepositoryActionListener {
    private static final String TAG = "AddFriendActivity";

    private RecyclerView mContactRecyclerView;
    private RecyclerView.Adapter mContactAdapter;
    private Button mAddFriendsButton;

    private FriendRepository mFriendRepository;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);

        mFriendRepository = new FriendRepository(this);
        mContactRecyclerView = findViewById(R.id.contacts_recycler_view);
        mContactRecyclerView.setHasFixedSize(true);
        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Contact> contactList = getContacts();
        List<Contact> sortedContacts = contactList.stream().sorted((obA, obB) ->
                obA.name.compareTo(obB.name)).collect(Collectors.toList());
        mContactAdapter = new ContactAdapter(new ArrayList<>(sortedContacts), this);
        mContactRecyclerView.setAdapter(mContactAdapter);

        mAddFriendsButton = findViewById(R.id.button_add_friends);
        mAddFriendsButton.setOnClickListener(v -> {
            List<Contact> selectedContacts = ((ContactAdapter) mContactAdapter).getSelected();

            for(Contact contact: selectedContacts) {
                Friend friend = new Friend(contact.name, contact.phoneNumber);
                mFriendRepository.insertFriend(friend, AddFriendActivity.this);
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();

        FriendsWithDebtsWrapper friendsWithDebtsWrapper = (FriendsWithDebtsWrapper) getIntent()
                .getSerializableExtra(FriendsFragment.FriendListExtra);
        ArrayList<Friend> currentFriends = friendsWithDebtsWrapper
                .getFriends()
                .stream()
                .map(fwd -> new Friend(fwd.friend.name, fwd.friend.phoneNumber))
                .collect(Collectors.toCollection(ArrayList::new));

        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = null;

                Integer hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor pCursor = contentResolver
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);

                    while (pCursor.moveToNext()) {
                        if (TextUtils.isEmpty(phone)) {
                            phone = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }

                    pCursor.close();
                }

                if (TextUtils.isEmpty(phone)) {
                    continue;
                }

                // Log.d(TAG, "getContacts: contact -> " + ": id = " + id + "; name = " + name + "; phone = " + phone);
                boolean exists = false;
                for (Friend friend: currentFriends) {
                    exists |= name.equals(friend.name) && phone.equals(friend.phoneNumber);
                    if (exists) break;
                }

                Log.d(TAG, "getContacts: name = " + name + " " + exists);

                if (!exists) {
                    contacts.add(new Contact(id, name, phone));
                }
            }
            cursor.close();
        }

        return contacts;
    }

    @Override
    public void actionSuccess() {

    }

    @Override
    public void actionFailed(String message) {

    }
}
