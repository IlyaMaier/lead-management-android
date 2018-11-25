package com.community.jboss.leadmanagement.main.groups;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.CustomDialogBox;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.SettingsActivity;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

@SuppressWarnings("ALL")
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> implements Filterable {

    private List<Groups> mGroups;
    private List<Groups> mSpare;
    public AdapterListener mListener;
    private Activity mActivity;

    @Override
    public Filter getFilter() {
        mGroups = mSpare;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String data = constraint.toString();
                if (data.isEmpty()) {
                    mGroups = mSpare;
                }
                List<Groups> filteredList = new ArrayList<>();
                for (Groups groups : mGroups) {
                    if (groups.getName().toLowerCase().contains(data.toLowerCase()))
                        filteredList.add(groups);
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mGroups = (ArrayList<Groups>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onGroupsDeleted(Groups groups);
    }

    GroupsAdapter(AdapterListener mListener, Activity mActivity) {
        this.mListener = mListener;
        this.mActivity = mActivity;
    }

    @SuppressLint("InflateParams")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_cell, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mGroups.get(position));
    }

    @Override
    public int getItemCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    void replaceData(List<Groups> groups) {
        this.mGroups = groups;
        this.mSpare = groups;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_avatar)
        CircularImageView avatar;
        @BindView(R.id.group_name)
        TextView name;
        @BindView(R.id.group_delete)
        ImageButton delete;

        private Groups mGroups;
        private SharedPreferences mPref;
        private Context mContext;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);

            if (mPref.getBoolean(SettingsActivity.PREF_DARK_THEME, false))
                name.setTextColor(Color.WHITE);

            itemView.setOnLongClickListener(view -> {
                final int newVisibility = delete.getVisibility() == View.VISIBLE
                        ? View.GONE
                        : View.VISIBLE;
                delete.setVisibility(newVisibility);
                if (mPref.getBoolean(SettingsActivity.PREF_DARK_THEME, false)) {
                    name.setTextColor(Color.WHITE);
                    delete.setBackgroundColor(Color.parseColor("#303030"));
                    delete.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_close_white_24dp));
                }
                return true;
            });

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, PopUpActivity.class);
                intent.putExtra("id", mGroups.getId());

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(mActivity, new Pair<>(name, mContext.getString(R.string.name)),
                                new Pair<>(avatar, mContext.getString(R.string.avatar)));
                mContext.startActivity(intent, options.toBundle());
            });

            delete.setOnClickListener(v1 -> {
                CustomDialogBox dialogBox = new CustomDialogBox();
                dialogBox.showAlert((Activity) mContext, null, null, false, mGroups, GroupsAdapter.this);
                delete.setVisibility(View.INVISIBLE);
            });
        }

        void bind(Groups groups) {
            mGroups = groups;
            name.setText(mGroups.getName());
            Glide.with(mContext).load(bytesToBitmap(mGroups.getPhoto())).into(avatar);
        }

    }

}
