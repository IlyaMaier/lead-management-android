package com.community.jboss.leadmanagement.main.contacts.importcontact;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.community.jboss.leadmanagement.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportsAdapter extends RecyclerView.Adapter<ImportsAdapter.ViewHolder> {
    private List<ImportContact> mDataset;
    private List<ImportContact> contactsToImport = new ArrayList<>();
    private boolean useDarkTheme;

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.impContactImg)
        ImageView contactImg;
        @BindView(R.id.impContactName)
        TextView name;
        @BindView(R.id.impContactNumber)
        TextView number;

        private ImportContact mContact;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        private void bind(ImportContact contact) {
            this.mContact = contact;
            if (!mContact.isChecked()) {
                if (!useDarkTheme)
                    contactImg.setBackgroundResource(R.drawable.ic_unchecked_24dp);
                else contactImg.setBackgroundResource(R.drawable.ic_unchecked_24dp_night);
            } else {
                if (!useDarkTheme)
                    contactImg.setBackgroundResource(R.drawable.ic_checked_24dp);
                else contactImg.setBackgroundResource(R.drawable.ic_checked_24dp_night);
            }
            name.setText(contact.getName());
            number.setText(contact.getNumber());
        }

        @Override
        public void onClick(View v) {
            if (mContact.isChecked()) {
                contactsToImport.remove(mContact);
                mContact.setChecked(false);
                if (!useDarkTheme)
                    contactImg.setBackgroundResource(R.drawable.ic_unchecked_24dp);
                else contactImg.setBackgroundResource(R.drawable.ic_unchecked_24dp_night);
            } else {
                contactsToImport.add(mContact);
                mContact.setChecked(true);
                if (!useDarkTheme)
                    contactImg.setBackgroundResource(R.drawable.ic_checked_24dp);
                else contactImg.setBackgroundResource(R.drawable.ic_checked_24dp_night);
            }
        }
    }

    ImportsAdapter(List<ImportContact> myDataset, boolean useDarkTheme) {
        mDataset = myDataset;
        this.useDarkTheme = useDarkTheme;
    }

    @NonNull
    @Override
    public ImportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_contact_cell, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public List<ImportContact> getContactsToImport() {
        return this.contactsToImport;
    }
}

