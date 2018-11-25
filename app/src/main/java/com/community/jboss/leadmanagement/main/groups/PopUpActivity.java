package com.community.jboss.leadmanagement.main.groups;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.main.groups.editgroup.EditGroupActivity;
import com.community.jboss.leadmanagement.main.groups.editgroup.EditGroupContact;
import com.community.jboss.leadmanagement.utils.DbUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;
import static com.community.jboss.leadmanagement.main.contacts.editcontact.EditContactActivity.bytesToBitmap;

public class PopUpActivity extends AppCompatActivity {

    @BindView(R.id.popup_activity_layout)
    LinearLayout layout;
    @BindView(R.id.popup_activity_layout2)
    LinearLayout layout2;
    @BindView(R.id.popup_activity_avatar)
    CircularImageView avatar;
    @BindView(R.id.popup_activity_name)
    TextView name;
    @BindView(R.id.popup_close)
    TextView close;
    @BindView(R.id.popup_activity_edit)
    Button edit;
    @BindView(R.id.rv_popup_groups)
    RecyclerView recyclerView;

    private Groups mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        ButterKnife.bind(this);

        mGroups = DbUtil.groupDao(getApplication()).getGroup(getIntent().getStringExtra("id"));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        initViews();
        initRV();
        if (useDarkTheme) setDarkTheme();
    }

    void initViews() {
        name.setText(mGroups.getName());
        Glide.with(getApplicationContext()).load(bytesToBitmap(mGroups.getPhoto())).into(avatar);
        close.setOnClickListener(view -> finish());
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditGroupActivity.class);
            intent.putExtra("id", mGroups.getId());

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, new Pair<>(name, getApplicationContext().getString(R.string.name)),
                            new Pair<>(avatar, getString(R.string.avatar)));
            startActivity(intent, options.toBundle());
            finish();
        });
    }

    void initRV() {
        PopUpAdapter adapter = new PopUpAdapter();
        setAdapterData(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    void setAdapterData(PopUpAdapter adapter) {
        List<EditGroupContact> editGroupContacts = new ArrayList<>();
        List<String> ids = mGroups.getContacts();
        ContactDao contactDao = DbUtil.contactDao(getApplication().getApplicationContext());
        final ContactNumberDao dao = DbUtil.contactNumberDao(getApplicationContext());
        for (int i = 0; i < ids.size(); i++) {
            Contact contact = contactDao.getContact(ids.get(i));
            editGroupContacts.add(new EditGroupContact(contact.getId(),
                    contact.getName(), dao.getContactNumbers(contact.getId()).get(0).getNumber()
                    , contact.getImage()));
        }
        adapter.replaceData(editGroupContacts);
    }

    void setDarkTheme() {
        layout.setBackgroundColor(Color.parseColor("#303030"));
        layout2.setBackgroundColor(Color.parseColor("#505050"));
        name.setTextColor(Color.WHITE);
        close.setBackground(getResources().getDrawable(R.drawable.ic_close_white_24dp));
    }

}
