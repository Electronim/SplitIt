package com.example.splitit.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Contact;
import com.example.splitit.ui.adapter.ContactAdapter;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {
    private RecyclerView mContactRecyclerView;
    private RecyclerView.Adapter mContactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);

        mContactRecyclerView = findViewById(R.id.contacts_recycler_view);
        mContactRecyclerView.setHasFixedSize(true);

        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mContactAdapter = new ContactAdapter(new ArrayList<Contact>());
        mContactRecyclerView.setAdapter(mContactAdapter);
    }
}
