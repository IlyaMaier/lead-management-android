package com.community.jboss.leadmanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.main.contacts.ContactsAdapter;
import com.community.jboss.leadmanagement.main.groups.GroupsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomDialogBox {

    @BindView(R.id.dialog_yes)
    Button yes;
    @BindView(R.id.dialog_no)
    Button no;
    @BindView(R.id.dialog_layout)
    RelativeLayout layout;
    @BindView(R.id.dialog_txt)
    TextView text;
    @BindView(R.id.dialog_img)
    ImageView img;

    private Contact mContact;
    private ContactsAdapter mAdapter;
    private Dialog mDialog;
    private Groups mGroups;
    private boolean mIsContact;
    private GroupsAdapter mGroupsAdapter;

    public void showAlert(Activity activity, final Contact contact, ContactsAdapter adapter, boolean isContact, final Groups groups, GroupsAdapter groupsAdapter) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(dialog.getContext()).inflate(R.layout.custom_dialog_box, null, false);
        ButterKnife.bind(this, view);
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(dialog.getContext());
        mContact = contact;
        mAdapter = adapter;
        mDialog = dialog;
        mGroups = groups;
        mIsContact = isContact;
        mGroupsAdapter = groupsAdapter;

        if (mPref.getBoolean(SettingsActivity.PREF_DARK_THEME, false)) {
            layout.setBackgroundColor(Color.parseColor("#303030"));
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(Color.parseColor("#303030"));
            yes.setBackgroundColor(Color.GRAY);
            yes.setTextColor(Color.WHITE);
            no.setTextColor(Color.WHITE);
            no.setBackgroundColor(Color.GRAY);
        }

        if (!mIsContact) {
            text.setText(R.string.delete_group);
            img.setContentDescription(activity.getString(R.string.delete_group));
        }

        dialog.setContentView(view);
        dialog.show();
    }

    @OnClick(R.id.dialog_yes)
    void onYesClicked() {
        if (mIsContact)
            mAdapter.mListener.onContactDeleted(mContact);
        else
            mGroupsAdapter.mListener.onGroupsDeleted(mGroups);
        mDialog.dismiss();
    }

    @OnClick(R.id.dialog_no)
    void onNoClicked() {
        mDialog.dismiss();
    }

}
