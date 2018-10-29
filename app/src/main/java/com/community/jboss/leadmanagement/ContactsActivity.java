package com.community.jboss.leadmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.community.jboss.leadmanagement.main.contacts.ContactsFragment;

public class ContactsActivity extends AppCompatActivity {

    public static  String C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        C = "c";
        getSupportFragmentManager().beginTransaction().replace(
                R.id.contacts_frame, new ContactsFragment()
        ).commit();
    }

}
