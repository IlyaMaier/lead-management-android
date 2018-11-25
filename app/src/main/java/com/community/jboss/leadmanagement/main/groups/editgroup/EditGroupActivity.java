package com.community.jboss.leadmanagement.main.groups.editgroup;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.main.groups.PopUpActivity;
import com.community.jboss.leadmanagement.utils.DbUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bitmapToBytes;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class EditGroupActivity extends AppCompatActivity {

    @BindView(R.id.group_toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.group_name)
    TextInputEditText name;
    @BindView(R.id.group_image)
    CircularImageView image;
    @BindView(R.id.rv_groups_edit)
    RecyclerView recyclerView;

    private EditGroupActivityViewModel mViewModel;
    private EditGroupAdapter mAdapter;
    private Groups mGroups;
    private boolean isNew = true;
    private final int IMAGE_FROM_GALLERY = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        if (useDarkTheme) setTheme(R.style.AppTheme_BG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(EditGroupActivityViewModel.class);

        checkIntent();
        initToolbar();
        initRV();

        if (useDarkTheme)
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white_24dp));

        image.setOnClickListener(view -> startActivityForResult(new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMAGE_FROM_GALLERY));
    }

    void checkIntent() {
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            setTitle(R.string.edit_group);
            isNew = false;
            mGroups = mViewModel.getGroup(id);
            name.setText(mGroups.getName());
            Glide.with(getApplicationContext()).load(bytesToBitmap(mGroups.getPhoto())).into(image);
        } else setTitle(R.string.add_group);
    }

    void initToolbar() {
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void setAdapterData() {
        List<EditGroupContact> c = new ArrayList<>();
        List<EditGroupContact> cToAdd = new ArrayList<>();
        if (isNew)
            mViewModel.getContacts().observe(this, contacts -> {
                if (contacts != null) {
                    for (int i = 0; i < contacts.size(); i++) {
                        Contact contact = contacts.get(i);
                        c.add(new EditGroupContact(contact.getId(), contact.getName(), getNumber(contact), contact.getImage()));
                    }
                }
            });
        else {
            List<String> ids = mGroups.getContacts();
            mViewModel.getContacts().observe(this, contacts -> {
                if (contacts != null) {
                    for (int i = 0; i < contacts.size(); i++) {
                        Contact contact = contacts.get(i);
                        EditGroupContact egc = new EditGroupContact(contact.getId(), contact.getName(), getNumber(contact), contact.getImage());
                        if (ids.contains(contact.getId())) {
                            egc.setChecked(true);
                            cToAdd.add(egc);
                        }
                        c.add(egc);
                    }
                }
            });
        }
        mAdapter.replaceData(c, cToAdd);
    }

    void initRV() {
        mAdapter = new EditGroupAdapter();
        setAdapterData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private String getNumber(Contact contact) {
        final ContactNumberDao dao = DbUtil.contactNumberDao(getApplicationContext());
        return dao.getContactNumbers(contact.getId()).get(0).getNumber();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!isNew) {
            Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
            intent.putExtra("id", mGroups.getId());

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, new Pair<>(name, getString(R.string.name)),
                            new Pair<>(image, getString(R.string.avatar)));
            startActivity(intent, options.toBundle());
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveGroup();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveGroup() {
        if (valid()) {
            List<String> ids = new ArrayList<>();
            List<EditGroupContact> editGroupContacts = mAdapter.getContactsToAdd();
            for (int i = 0; i < editGroupContacts.size(); i++)
                ids.add(editGroupContacts.get(i).getId());
            if (isNew) {
                mGroups = new Groups(name.getText().toString(),
                        bitmapToBytes(((BitmapDrawable) image.getDrawable()).getBitmap()),
                        ids);
                mViewModel.saveGroup(true, mGroups);
            } else {
                mGroups.setContacts(ids);
                mGroups.setName(name.getText().toString());
                mGroups.setPhoto(bitmapToBytes(((BitmapDrawable) image.getDrawable()).getBitmap()));
                mViewModel.saveGroup(false, mGroups);

                Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
                intent.putExtra("id", mGroups.getId());

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this, new Pair<>(name, getString(R.string.name)),
                                new Pair<>(image, getString(R.string.avatar)));
                startActivity(intent, options.toBundle());
            }
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_contact, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Glide.with(this).load(bitmap).apply(new RequestOptions().circleCrop()).into(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean valid() {
        boolean valid = true;
        if (mAdapter.getContactsToAdd().size() == 0) {
            valid = false;
            Toast.makeText(this, "Choose at least 1 contact", Toast.LENGTH_SHORT).show();
        }
        if (name.getText().toString().isEmpty()) {
            valid = false;
            name.setError("Fill in");
        }
        return valid;
    }

}
