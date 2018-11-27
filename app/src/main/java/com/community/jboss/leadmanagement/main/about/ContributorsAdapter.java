package com.community.jboss.leadmanagement.main.about;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorsViewHolder> {

    private List<Contributor> contributors;
    private Context context;

    ContributorsAdapter(List<Contributor> contributors, Context context) {
        this.contributors = contributors;
        this.context = context;
    }

    @NonNull
    @Override
    public ContributorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContributorsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_contributors, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorsViewHolder contributorsViewHolder, int i) {
        Contributor contributor = contributors.get(i);
        contributorsViewHolder.name.setText(contributor.getName());
        Glide.with(context).load(contributor.getPhoto()).into(contributorsViewHolder.photo);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    void updateContributors(List<Contributor> c) {
        contributors = c;
    }

    class ContributorsViewHolder extends RecyclerView.ViewHolder {

        CircularImageView photo;
        TextView name;

        ContributorsViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo_contributor);
            name = itemView.findViewById(R.id.tv_name_contributor);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
            if (useDarkTheme) name.setTextColor(Color.WHITE);
        }
    }

}