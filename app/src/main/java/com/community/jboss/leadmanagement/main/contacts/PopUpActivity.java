package com.community.jboss.leadmanagement.main.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.community.jboss.leadmanagement.PermissionManager;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class PopUpActivity extends AppCompatActivity {

    TextView txtClose;
    TextView popupName;
    TextView contactNum;
    TextView mail;
    TextView location;
    TextView notes;
    TextView notes_hint;
    Button btnEdit;
    Button btnCall;
    Button btnMsg;
    LinearLayout layout;
    ImageView image;

    ImageView helper_email;
    ImageView helper_phone;
    ImageView helper_adress;
    ImageView helper_notes;

    SharedPreferences mPref;
    PermissionManager permManager;
    String name, number, email, mNotes;
    byte[] photo;
    public static final int RC_EDIT = 987;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_detail);

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        email = getIntent().getStringExtra("email");
        mNotes = getIntent().getStringExtra("notes");
        photo = getIntent().getByteArrayExtra("photo");

        txtClose = findViewById(R.id.txt_close);
        btnEdit = findViewById(R.id.btn_edit);
        popupName = findViewById(R.id.popup_name);
        contactNum = findViewById(R.id.txt_num);
        btnCall = findViewById(R.id.btn_call);
        btnMsg = findViewById(R.id.btn_msg);
        mail = findViewById(R.id.popupMail);
        layout = findViewById(R.id.popupLayout);
        image = findViewById(R.id.details_image);
        location = findViewById(R.id.details_adress);
        notes = findViewById(R.id.details_note);
        notes_hint = findViewById(R.id.details_note_hint);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        permManager = new PermissionManager(this, this);

        helper_email = findViewById(R.id.popup_helper_email);
        helper_phone = findViewById(R.id.popup_helper_phone);
        helper_adress = findViewById(R.id.popup_helper_adress);
        helper_notes = findViewById(R.id.popup_helper_note);

        if (mPref.getBoolean(PREF_DARK_THEME, false)) {
            layout.setBackgroundColor(Color.parseColor("#303030"));
            popupName.setTextColor(Color.WHITE);
            contactNum.setTextColor(Color.WHITE);
            mail.setTextColor(Color.WHITE);
            location.setTextColor(Color.WHITE);
            notes.setTextColor(Color.WHITE);
            notes_hint.setTextColor(Color.WHITE);

            txtClose.setBackground(getResources().getDrawable(R.drawable.ic_close_white));
            helper_email.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_white));
            helper_phone.setImageDrawable(getResources().getDrawable(R.drawable.ic_phone_white));
            helper_adress.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_white));
            helper_notes.setImageDrawable(getResources().getDrawable(R.drawable.ic_notes_white));
        }

        popupName.setText(name);
        contactNum.setText(number);
        mail.setText(email);
        notes.setText(mNotes);
        Glide.with(this).load(bytesToBitmap(photo)).apply(new RequestOptions().circleCrop()).into(image);
        txtClose.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                finishAfterTransition();
            else finish();
        });
        btnEdit.setOnClickListener(view -> {
            final Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra(EditContactActivity.INTENT_EXTRA_CONTACT_NUM, number);
            startActivityForResult(intent, RC_EDIT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDIT && resultCode == Activity.RESULT_OK) {
            popupName.setText(data.getStringExtra("name"));
            contactNum.setText(data.getStringExtra("number"));
            mail.setText(data.getStringExtra("email"));
            notes.setText(data.getStringExtra("notes"));
            photo = data.getByteArrayExtra("photo");
            Glide.with(this).load(bytesToBitmap(photo)).apply(new RequestOptions().circleCrop()).into(image);
        }
    }

}

