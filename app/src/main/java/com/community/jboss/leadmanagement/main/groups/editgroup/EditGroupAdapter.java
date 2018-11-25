package com.community.jboss.leadmanagement.main.groups.editgroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class EditGroupAdapter extends RecyclerView.Adapter<EditGroupAdapter.EditGroupViewHolder> {

    private List<EditGroupContact> mContacts;
    private List<EditGroupContact> mContactsToAdd;

    @Override
    public EditGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditGroupViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_group_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(EditGroupViewHolder holder, int position) {
        final EditGroupContact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    void replaceData(List<EditGroupContact> contacts, List<EditGroupContact> contactsToAdd) {
        mContacts = contacts;
        mContactsToAdd = contactsToAdd;
        notifyDataSetChanged();
    }

    List<EditGroupContact> getContactsToAdd() {
        return mContactsToAdd;
    }

    class EditGroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.edit_group_name)
        TextView name;
        @BindView(R.id.edit_group_number)
        TextView number;
        @BindView(R.id.edit_group_avatar)
        CircularImageView avatar;
        @BindView(R.id.edit_group_check)
        ImageView check;

        private EditGroupContact mContact;
        private Context mContext;

        EditGroupViewHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);

            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (mPref.getBoolean(PREF_DARK_THEME, false)) {
                name.setTextColor(Color.WHITE);
                number.setTextColor(Color.WHITE);
            }

            itemView.setOnClickListener(view -> {
                if (mContact.isChecked()) {
                    mContactsToAdd.remove(mContact);
                    mContact.setChecked(false);
                    check.setBackgroundResource(R.drawable.ic_unchecked_24dp);
                } else {
                    mContactsToAdd.add(mContact);
                    mContact.setChecked(true);
                    check.setBackgroundResource(R.drawable.ic_checked_24dp);
                }
            });
        }

        void bind(EditGroupContact contact) {
            mContact = contact;
            name.setText(contact.getName());
            number.setText(contact.getNumber());
            Glide.with(mContext).load(bytesToBitmap(contact.getPhoto())).into(avatar);

            if (contact.isChecked()) check.setBackgroundResource(R.drawable.ic_checked_24dp);
            else check.setBackgroundResource(R.drawable.ic_unchecked_24dp);
        }

    }

}
