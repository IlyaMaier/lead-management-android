package com.community.jboss.leadmanagement.main.contacts.editcontact;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class EditContactActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_CONTACT_NUM = "INTENT_EXTRA_CONTACT_NUM";

    @BindView(R.id.add_contact_toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.contact_photo)
    CircularImageView contactPhoto;
    @BindView(R.id.contact_name_field)
    TextInputEditText contactNameField;
    @BindView(R.id.contact_number_field)
    TextInputEditText contactNumberField;
    @BindView(R.id.contact_email_field)
    TextInputEditText contactEmailField;

    private EditContactActivityViewModel mViewModel;
    public static final int REQUEST_CODE_EDIT_CONTACT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_BG);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        ButterKnife.bind(this);

        locationField.setHint(Html.fromHtml(getString(R.string.location)+" <small>(optional)</small>", Html.FROM_HTML_MODE_LEGACY));

        if(useDarkTheme) {
            setDrawableLeft(locationField, R.drawable.ic_location_white);
            setDrawableLeft(emailField, R.drawable.ic_email_white);
            setDrawableLeft(contactNameField, R.drawable.ic_person_white);
            setDrawableLeft(contactNumberField, R.drawable.ic_phone_white);
            setDrawableLeft(queryField, R.drawable.ic_question_white);
            setDrawableLeft(notesField, R.drawable.ic_notes_white);
        }


        mViewModel = ViewModelProviders.of(this).get(EditContactActivityViewModel.class);
        mViewModel.getContact().observe(this, contact -> {
            if (contact == null || mViewModel.isNewContact()) {
                setTitle(R.string.title_add_contact);
            } else {
                setTitle(R.string.title_edit_contact);
                contactNameField.setText(contact.getName());
                contactEmailField.setText(contact.getEmail());
                Glide.with(getApplicationContext()).load(contact.getPhoto()).into(contactPhoto);
            }
        });
        mViewModel.getContactNumbers().observe(this, contactNumbers -> {
            if (contactNumbers == null || contactNumbers.isEmpty()) {
                return;
            }
            // Get only the first one for now
            final ContactNumber contactNumber = contactNumbers.get(0);
            contactNumberField.setText(contactNumber.getNumber());
        });

        final Intent intent = getIntent();
        final String number = intent.getStringExtra(INTENT_EXTRA_CONTACT_NUM);
        if (mViewModel.getContactNumberByNumber(number) != null) {
            mViewModel.setContact(mViewModel.getContactNumberByNumber(number).getContactId());
        } else {
            mViewModel.setContact(null);
            contactNumberField.setText(number);
        }

        contactPhoto.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), REQUEST_CODE_EDIT_CONTACT));

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        contact_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMAGE_FROM_GALLERY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_CONTACT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                Glide.with(getApplicationContext()).load(bitmap).into(contactPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveContact();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_contact, menu);
        return true;
    }

    //TODO Add multiple numbers
    private void saveContact() {
        // Check is Name or Password is empty
        if (!checkEditText(contactNameField, "Please enter name") || !checkNo(contactNumberField, "Enter Correct no.")
                || !checkEditText(contactNumberField, "Please enter number")) {
            return;
        }

        final String name = contactNameField.getText().toString();
        final String email = contactEmailField.getText().toString();
        final byte[] photo = getBytesFromBitmap(((BitmapDrawable) contactPhoto.getDrawable()).getBitmap());
        mViewModel.saveContact(name, email, photo);

        final String number = contactNumberField.getText().toString();
        mViewModel.saveContactNumber(number);
        mViewModel.saveData(email, location, query, image, notes);

        finish();
    }

    private byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
    }

    private boolean checkEditText(EditText editText, String errorStr) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(errorStr);
            return false;
        }

        return true;
    }

    private boolean checkNo(EditText editText, String errorStr) {
        if (editText.getText().toString().length() < 4) {
            editText.setError(errorStr);
            return false;
        }
        return true;
    }

    private boolean checkInputs(){
        boolean status = true;

        if(checkEditText(emailField, "Please enter mail")){
            if(!emailField.getText().toString().contains("@")){
                emailField.setError("Wrong mail formatting");
                status = false;
            }
        }

        if(!checkEditText(contactNumberField, "Please enter mobile number")){
            status = false;
        }
        if(!checkEditText(contactNameField, "Please enter full name")){
            status = false;
        }
        if(!checkEditText(queryField, "Please enter query")){
            status = false;
        }

        return status;
    }

    private void setDrawableLeft(TextInputEditText field, int id){
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, 60, 60);
        field.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Glide.with(this).load(bitmap).apply(new RequestOptions().circleCrop()).into(contact_logo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
