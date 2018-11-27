package com.community.jboss.leadmanagement.main.about;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.jboss.leadmanagement.AboutActivity;
import com.community.jboss.leadmanagement.BuildConfig;
import com.community.jboss.leadmanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class InfoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.version)
    TextView version;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        final View view = inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this, view);

        if (useDarkTheme) {
            view.setBackgroundColor(Color.parseColor("#303030"));
            ((TextView) view.findViewById(R.id.tv_about)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_organization)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_license)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_contributors)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_help)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_rights)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(R.id.tv_name_info)).setTextColor(Color.WHITE);
            version.setTextColor(Color.WHITE);
        }

        version.setText(getString(R.string.version_s, BuildConfig.VERSION_NAME));

        view.findViewById(R.id.fl_about).setOnClickListener(this);
        view.findViewById(R.id.fl_organization).setOnClickListener(this);
        view.findViewById(R.id.fl_contributors).setOnClickListener(this);
        view.findViewById(R.id.fl_license).setOnClickListener(this);
        view.findViewById(R.id.fl_help).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fl_help) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitter.im/JBossOutreach/lead-management"));
            startActivity(myIntent);
            return;
        }

        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.fl_about:
                fragment = new AboutFragment();
                break;
            case R.id.fl_organization:
                fragment = new OrganizationFragment();
                break;
            case R.id.fl_contributors:
                fragment = new ContributorsFragment();
                break;
            case R.id.fl_license:
                fragment = new LicenseFragment();
                break;
        }
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().replace(R.id.frame_about, fragment).commit();
            AboutActivity.b = true;
        }
    }

}
