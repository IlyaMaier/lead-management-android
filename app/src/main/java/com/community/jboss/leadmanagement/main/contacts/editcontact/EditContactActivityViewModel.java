package com.community.jboss.leadmanagement.main.contacts.editcontact;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.ContactNumberDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.ContactNumber;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class EditContactActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Contact> mContact;
    private MutableLiveData<List<ContactNumber>> mContactNumbers;
    private boolean mIsNewContact;

    public EditContactActivityViewModel(@NonNull Application application) {
        super(application);

        mContact = new MutableLiveData<>();
        mContactNumbers = new MutableLiveData<>();
    }

    boolean isNewContact() {
        return mIsNewContact;
    }

    public LiveData<Contact> getContact() {
        return mContact;
    }

    public void setContact(String contactId) {
        final Context context = getApplication();

        final ContactDao contactDao = DbUtil.contactDao(context);
        if (contactDao != null) {
            Contact contact = contactDao.getContact(contactId);
            if (contact == null) {
                contact = new Contact(null);
                mIsNewContact = true;
            }
            mContact.setValue(contact);
        } else {
            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
            String TAG = "EditContactViewModel";
            Log.d(TAG, "setContact: contactcDao is null");
        }
        final ContactNumberDao contactNumberDao = DbUtil.contactNumberDao(context);
        List<ContactNumber> contactNumbers = contactNumberDao.getContactNumbers(contactId);
        if (contactNumbers == null) {
            contactNumbers = new ArrayList<>();
        }
        mContactNumbers.setValue(contactNumbers);
    }

    LiveData<List<ContactNumber>> getContactNumbers() {
        return mContactNumbers;
    }

    ContactNumber getContactNumberByNumber(String number) {
        final ContactNumberDao contactNumberDao = DbUtil.contactNumberDao(getApplication());
        return contactNumberDao.getContactNumber(number);
    }

    void saveContact(String name) {
        final ContactDao dao = DbUtil.contactDao(getApplication());

        Contact contact = mContact.getValue();
        if (contact == null) {
            contact = new Contact(name);
        } else {
            contact.setName(name);
        }

        if (mIsNewContact) {
            dao.insert(contact);
        } else {
            dao.update(contact);
        }
    }

    void saveContactNumber(String number) {
        final ContactNumberDao dao = DbUtil.contactNumberDao(getApplication());
        String contactId = "";
        try {
             contactId = mContact.getValue().getId();
        } catch (NullPointerException e) {
            Toast.makeText(getApplication(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        final List<ContactNumber> contactNumbers = mContactNumbers.getValue();

        ContactNumber contactNumber;
        if (contactNumbers == null || contactNumbers.isEmpty()) {
            contactNumber = new ContactNumber(number, contactId);
        } else {
            contactNumber = mContactNumbers.getValue().get(0);
            contactNumber.setNumber(number);
        }

        if (mIsNewContact) {
            dao.insert(contactNumber);
        } else {
            dao.update(contactNumber);
        }
    }
}
