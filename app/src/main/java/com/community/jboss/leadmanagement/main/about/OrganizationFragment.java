package com.community.jboss.leadmanagement.main.about;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.jboss.leadmanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class OrganizationFragment extends Fragment {

    @BindView(R.id.tv_fragment_organization)
    TextView textView;

    public OrganizationFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        View view = inflater.inflate(R.layout.fragment_organization, container, false);
        ButterKnife.bind(this, view);
        textView.setText(Html.fromHtml("<h3>JBoss Community is a community of open source projects primarily written in Java.</h3>\n\n" +
                "JBoss Community is a community of open source projects. The community hosts a large number of projects that are written in various programming languages. The primary language is Java. But there are also projects that are written in Ruby, PHP, Node and other languages.\n\n" +
                "Project categories range from better testing support over IDEs, application servers, application and performance monitoring to micro-services.\n\n" +
                "<b>Primary Open Source License:</b> Apache License 2.0 (Apache-2.0)"));
        if (useDarkTheme) {
            view.findViewById(R.id.fragment_organization).setBackgroundColor(Color.parseColor("#303030"));
            textView.setTextColor(Color.WHITE);
        }
        return view;
    }

}
