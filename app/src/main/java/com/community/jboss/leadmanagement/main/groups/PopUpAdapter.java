package com.community.jboss.leadmanagement.main.groups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.main.groups.editgroup.EditGroupContact;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class PopUpAdapter extends RecyclerView.Adapter<PopUpAdapter.ViewHolder> {

    private List<EditGroupContact> mContacts;

    @SuppressLint("InflateParams")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    void replaceData(List<EditGroupContact> contacts) {
        mContacts = contacts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contact_avatar)
        CircularImageView avatar;
        @BindView(R.id.contact_name)
        TextView name;
        @BindView(R.id.contact_number)
        TextView number;

        private Context mContext;
        private SharedPreferences mPref;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (mPref.getBoolean(PREF_DARK_THEME, false)) {
                name.setTextColor(Color.WHITE);
                number.setTextColor(Color.WHITE);
            }

        }

        void bind(EditGroupContact contact) {
            name.setText(contact.getName());
            number.setText(contact.getNumber());
            Glide.with(mContext).load(bytesToBitmap(contact.getPhoto())).into(avatar);
        }

    }

}
