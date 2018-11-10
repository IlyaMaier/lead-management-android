package com.community.jboss.leadmanagement.main.contacts.importcontact;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class ImportContactActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener{

    @BindView(R.id.importContactRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.text_no_result)
    TextView textView;

    ContactDao contactDao;
    ContactNumberDao numberDao;
    private ImportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
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

        textView.setVisibility(View.GONE);

        adapter = new ImportsAdapter(getContacts(),getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_contact_menu,menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search_import);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryRefinementEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if(item==searchMenuItem){
                    adapter.getFilter().filter(searchView.getQuery());
                    if( adapter.getItemCount() == 0){
                        textView.setVisibility(View.VISIBLE);
                    } else{
                        textView.setVisibility(View.GONE);
                    }
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if(item==searchMenuItem){
                    adapter.getFilter().filter("");
                    textView.setVisibility(View.GONE);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.finishImport){
            List<ImportContact> contacts = adapter.getContactsToImport();
            if(contacts.size()==0){
                onBackPressed();
            }else{
                for(ImportContact contact:contacts){
                    String contactUUID = UUID.randomUUID().toString();
                    contactDao.insert(new Contact(contactUUID,contact.getName()));
                    numberDao.insert(new ContactNumber(contact.getNumber(),contactUUID));
                }
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public List<ImportContact> getContacts(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        List<ImportContact> contacts = new ArrayList<>();

        if(people.moveToFirst()) {
            do {
                String name   = people.getString(indexName);
                String number = people.getString(indexNumber)
                        .replace("(","")
                        .replace(")","")
                        .replace(" ","")
                        .replace("-","");
                if(numberDao.getContactNumber(number)==null) {
                    ImportContact contact = new ImportContact(name, number);
                    contacts.add(contact);
                }
            } while (people.moveToNext());
        }
        people.close();
        return contacts;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search_import);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        if (adapter.getItemCount() == 0){
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.GONE);
        }
        return true;
    }

}

