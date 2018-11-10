package com.community.jboss.leadmanagement.main.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Color;

import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.CustomDialogBox;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.SettingsActivity;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.utils.DbUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {
    private List<Contact> mContacts;
    private ContactsAdapter mAdapter;
    public AdapterListener mListener;
    private List<Contact> spareData;
    private Activity activity;

    public ContactsAdapter(AdapterListener listener,Activity activity) {
        mListener = listener;
        mAdapter = this;
        mContacts = new ArrayList<>();
        spareData = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_cell, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void replaceData(List<Contact> contacts) {
        mContacts = contacts;
        spareData = contacts;
        notifyDataSetChanged();
    }

    private Context mContext;
    @Override
    public Filter getFilter() {
        mContacts = spareData;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String data = constraint.toString();
                if(data.isEmpty()){
                    mContacts = spareData;
                }
                List<Contact> filteredList = new ArrayList<>();
                final ContactNumberDao dao = DbUtil.contactNumberDao(mContext);

                for(Contact contact: mContacts){
                    if(contact.getName().toLowerCase().contains(data.toLowerCase())){
                        filteredList.add(contact);
                    }
                    else if (dao.getContactNumbers(contact.getId()).get(0).getNumber().contains(data)) {
                        filteredList.add(contact);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mContacts = (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onContactDeleted(Contact contact);
    }

    final class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.contact_name)
        TextView name;
        @BindView(R.id.contact_number)
        TextView number;
        @BindView(R.id.contact_avatar)
        CircularImageView picture;
        @BindView(R.id.contact_delete)
        ImageButton deleteButton;


        private Contact mContact;
        private Context mContext;
        private SharedPreferences mPref;

        ViewHolder(View v) {
            super(v);

            mContext = v.getContext();
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            ButterKnife.bind(this, v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            deleteButton.setOnClickListener(v1 -> {
                CustomDialogBox dialogBox = new CustomDialogBox();
                dialogBox.showAlert((Activity) mContext,mContact,mAdapter);
                deleteButton.setVisibility(View.INVISIBLE);
            });
        }

        void bind(Contact contact) {
            mContact = contact;

            // TODO add contact avatar
            name.setText(contact.getName());
            number.setText(getNumber());
            Glide.with(mContext).load(bytesToBitmap(contact.getImage())).into(picture);
        }

        /**
         * TODO:
         * This really sucks but it'll do until we decide to make
         * database transactions go into the background thread,
         * or find out how to embed the contact number into the
         * contact object itself
         */
        private String getNumber() {
            final ContactNumberDao dao = DbUtil.contactNumberDao(mContext);
            return dao.getContactNumbers(mContact.getId()).get(0).getNumber();
        }

        @Override
        public void onClick(View view) {
            final Context context = view.getContext();

            Intent intent = new Intent(context, PopUpActivity.class);
            intent.putExtra("image_name", 1);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("number", number.getText().toString());
            intent.putExtra("email", mContact.getMail());
            intent.putExtra("notes", mContact.getNotes());
            intent.putExtra("photo", mContact.getImage());
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, picture, context.getString(R.string.profile_image));
            context.startActivity(intent, options.toBundle());
        }

        @Override
        public boolean onLongClick(View view) {
            final int newVisibility = deleteButton.getVisibility() == View.VISIBLE
                    ? View.GONE
                    : View.VISIBLE;
            deleteButton.setVisibility(newVisibility);
            if(mPref.getBoolean(SettingsActivity.PREF_DARK_THEME,false)){
                deleteButton.setBackgroundColor(Color.parseColor("#303030"));
                deleteButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_close_white));
            }
            return true;
        }
    }

    public int getDataSize(){
        return mContacts.size();
    }
}

