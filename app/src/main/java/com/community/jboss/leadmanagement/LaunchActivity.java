package com.community.jboss.leadmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.community.jboss.leadmanagement.intro.IntroActivity;
import com.community.jboss.leadmanagement.main.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    private final String FIRST_TIME = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(FIRST_TIME, true)) {
            sharedPreferences.edit().putBoolean(FIRST_TIME, false).apply();
            final Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        } else {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
