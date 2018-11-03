package com.community.jboss.leadmanagement.main.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.community.jboss.leadmanagement.PermissionManager;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class PopUpActivity extends AppCompatActivity {

    TextView txtClose;
    TextView popupName;
    TextView contactNum;
    TextView mail;
    Button btnEdit;
    Button btnCall;
    Button btnMsg;
    LinearLayout layout;
    SharedPreferences mPref;
    PermissionManager permManager;
    String name, number;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_detail);

//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        LinearLayout layout = findViewById(R.id.popupLayout);
//        layout.getLayoutParams().width = (int) (screenWidth / 1.5);
//        layout.getLayoutParams().height = (int) (screenWidth / 1.5);
//        layout.requestLayout();

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");

        txtClose = findViewById(R.id.txt_close);
        btnEdit = findViewById(R.id.btn_edit);
        popupName = findViewById(R.id.popup_name);
        contactNum = findViewById(R.id.txt_num);
        btnCall = findViewById(R.id.btn_call);
        btnMsg = findViewById(R.id.btn_msg);
        mail = findViewById(R.id.popupMail);
        layout = findViewById(R.id.popupLayout);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        permManager = new PermissionManager(this, this);

        if (mPref.getBoolean(PREF_DARK_THEME, false)) {
            layout.setBackgroundColor(Color.parseColor("#303030"));
            popupName.setTextColor(Color.WHITE);
            contactNum.setTextColor(Color.WHITE);
            mail.setTextColor(Color.WHITE);
            txtClose.setBackground(getResources().getDrawable(R.drawable.ic_close_white));
        }

        popupName.setText(name);
        contactNum.setText(number);

        txtClose.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                finishAfterTransition();
            else finish();
        });

        btnEdit.setOnClickListener(view -> {
            final Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra(EditContactActivity.INTENT_EXTRA_CONTACT_NUM, number);
            startActivity(intent);
        });

        btnCall.setOnClickListener(view -> {
            if (permManager.permissionStatus(Manifest.permission.CALL_PHONE)) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            } else {
                permManager.requestPermission(58, Manifest.permission.CALL_PHONE);
            }
        });

        btnMsg.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + number))));

    }

}
