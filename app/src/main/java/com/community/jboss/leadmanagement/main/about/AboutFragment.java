package com.community.jboss.leadmanagement.main.about;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.jboss.leadmanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class AboutFragment extends Fragment {

    @BindView(R.id.tv_fragment_about)
    TextView textView;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        textView.setText(Html.fromHtml("<h1>lead-management-android</h1>\n" +
                "\n" +
                "<h2> Lead (sales lead)</h2>\n" +
                "A sales lead is a potential sales contact, an individual or organization that expresses an interest in your goods or services. Leads are typically obtained through the referral of an existing customer, or through a direct response to advertising or publicity. A company's marketing department is typically responsible for lead generation.\n" +
                "\n" +
                "<h4> How it works</h4>\n" +
                "<i>Lead-Management</i> in the call process shows a notification. If the user clicks this notification, they will simply be redirected to the application to save the contact. If the contact exists, you will see details of the caller, if not you will be redirected to adding this number as a contact. After this, the app stores data of the caller in the local database.\n" +
                "This app is currently in development, which means that in the future, many more features will be added.\n"));
        if (useDarkTheme) {
            view.findViewById(R.id.fragment_about).setBackgroundColor(Color.parseColor("#303030"));
            textView.setTextColor(Color.WHITE);
        }
        return view;
    }

}
