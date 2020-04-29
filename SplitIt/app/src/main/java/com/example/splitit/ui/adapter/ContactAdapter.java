package com.example.splitit.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Contact;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Contact> mContacts;

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    public ContactAdapter(ArrayList<Contact> mContacts, Context context) {
        this.mContext = context;
        this.mContacts = mContacts;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView contactCardView;
        TextView nameTextView;
        TextView phoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.contactCardView = itemView.findViewById(R.id.card_view_contact);
            this.nameTextView = itemView.findViewById(R.id.contact_name_text_view);
            this.phoneNumberTextView = itemView.findViewById(R.id.contact_phone_text_view);
        }

        public void bind(final Contact contact){
            this.nameTextView.setText(contact.name);
            this.phoneNumberTextView.setText(contact.phoneNumber);

            this.contactCardView.setOnClickListener(v -> {
                contact.changeSelectedState();
                this.contactCardView.toggle();
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Contact> getSelected() {
        return mContacts.stream()
                .filter(c -> c.isSelected)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
