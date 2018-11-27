package com.community.jboss.leadmanagement;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import com.community.jboss.leadmanagement.main.about.InfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_about)
    Toolbar toolbar;

    public static boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_about, new InfoFragment()).commit();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(v -> {
            if (b) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_about, new InfoFragment()).commit();
                b = false;
            } else finish();
        });
        toolbar.setTitle(R.string.about_app);

    }

    @Override
    public void onBackPressed() {
        if (b) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_about, new InfoFragment()).commit();
            b = false;
        } else super.onBackPressed();
    }

}
