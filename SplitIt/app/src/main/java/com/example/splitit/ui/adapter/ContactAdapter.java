package com.example.splitit.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Contact;

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
        CardView contactCardView;
        TextView nameTextView;
        TextView phoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.contactCardView = itemView.findViewById(R.id.contact_card_view);
            this.nameTextView = itemView.findViewById(R.id.contact_name_text_view);
            this.phoneNumberTextView = itemView.findViewById(R.id.contact_phone_text_view);
        }

        public void bind(final Contact contact){
            changeColor(contact);
            this.nameTextView.setText(contact.name);
            this.phoneNumberTextView.setText(contact.phoneNumber);

            contactCardView.setOnClickListener(v -> {
                contact.changeSelectedState();
                changeColor(contact);
            });
        }

        private void changeColor(final Contact contact) {
            if (contact.isSelected) {
                int colorCode = mContext.getResources().getColor(R.color.primaryColor);

                this.contactCardView.setCardBackgroundColor(colorCode);
                this.nameTextView.setTextColor(Color.WHITE);
                this.phoneNumberTextView.setTextColor(Color.WHITE);
            } else {
                int colorCode = mContext.getResources().getColor(R.color.secondaryTextColor);

                this.contactCardView.setCardBackgroundColor(Color.WHITE);
                this.nameTextView.setTextColor(colorCode);
                this.phoneNumberTextView.setTextColor(colorCode);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Contact> getSelected() {
        return mContacts.stream()
                .filter(c -> c.isSelected)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
