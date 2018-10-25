package com.community.jboss.leadmanagement;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.community.jboss.leadmanagement.main.MainActivity;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AnimationDrawable) getWindow().getDecorView().getBackground()).start();

        new Handler().postDelayed(() -> {
            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }, 2800);

    }
}
