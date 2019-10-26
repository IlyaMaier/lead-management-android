package com.community.jboss.leadmanagement.main.contacts.importcontact;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsFragment.PREF_DARK_THEME;

public class ImportContactActivity extends AppCompatActivity {

    @BindView(R.id.importContactRecycler)
    RecyclerView recyclerView;

    ContactDao contactDao;
    ContactNumberDao numberDao;
    private ImportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);
        setTitle(R.string.import_contacts);
        ButterKnife.bind(this);
        contactDao = DbUtil.contactDao(getApplication());
        numberDao = DbUtil.contactNumberDao(getApplication());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ImportsAdapter(getContacts(), useDarkTheme);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.finishImport) {
            List<ImportContact> contacts = adapter.getContactsToImport();
            if (contacts.size() == 0) {
                onBackPressed();
            } else {
                for (ImportContact contact : contacts) {
                    String contactUUID = UUID.randomUUID().toString();
                    Contact c = new Contact(contactUUID, contact.getName());
                    c.setImage(contact.getPhoto());
                    contactDao.insert(c);
                    numberDao.insert(new ContactNumber(contact.getNumber(), contactUUID));
                }
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public List<ImportContact> getContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        List<ImportContact> contacts = new ArrayList<>();

        if (people != null) {
            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoUri = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

            if (people.moveToFirst()) {
                do {
                    String name = people.getString(indexName);
                    String number = people.getString(indexNumber)
                            .replace("(", "")
                            .replace(")", "")
                            .replace(" ", "")
                            .replace("-", "");
                    String u = people.getString(photoUri);
                    if (numberDao.getContactNumber(number) == null) {
                        ImportContact contact = new ImportContact(name, number, getPhoto(getApplicationContext(), u));
                        contacts.add(contact);
                    }
                } while (people.moveToNext());
            }
            people.close();
        }
        return contacts;
    }

    public static byte[] getPhoto(Context context, String u) {
        Bitmap bitmap = null;
        if (u != null) {
            Uri uri = Uri.parse(u);
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_person_grey);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
