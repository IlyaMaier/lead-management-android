package com.community.jboss.leadmanagement;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.community.jboss.leadmanagement.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

        if(getActivity()!=null) {
            Activity mActivity = getActivity();
            final SharedPreferences sharedPref = mActivity.getPreferences(Context.MODE_PRIVATE);
            final String currentServer = sharedPref.getString(getString(R.string.saved_server_ip), "https://github.com/jboss-outreach");

            final EditTextPreference mPreference = (EditTextPreference) findPreference("server_location");
            final SwitchPreference mToggleMode = (SwitchPreference) findPreference("dark_theme");
            final Preference mSignOut = findPreference("sign_out");
            mToggleMode.setOnPreferenceChangeListener((preference, newValue) -> {
                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                mActivity.finish();
                return true;
            });
            mPreference.setSummary(currentServer);
            mPreference.setText(currentServer);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mSignOut.setOnPreferenceClickListener(preference -> {
                if(mAuth.getCurrentUser() == null) {
                    Toast.makeText(getContext(), "Already signed out", Toast.LENGTH_SHORT).show();
                    return true;
                }
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Sign Out");
                alertBuilder.setMessage("Are you sure you want to sign out?");
                alertBuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                alertBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
                });
                alertBuilder.create().show();
                return true;
            });
        }
    }
}
