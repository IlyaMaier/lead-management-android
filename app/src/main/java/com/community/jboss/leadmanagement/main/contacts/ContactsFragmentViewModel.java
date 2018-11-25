package com.community.jboss.leadmanagement.main.contacts;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.community.jboss.leadmanagement.data.daos.ContactDao;
import com.community.jboss.leadmanagement.data.daos.GroupDao;
import com.community.jboss.leadmanagement.data.entities.Contact;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.List;

public class ContactsFragmentViewModel extends AndroidViewModel {
    private LiveData<List<Contact>> mContacts;
    private LiveData<List<Groups>> mGroups;

    public ContactsFragmentViewModel(@NonNull Application application) {
        super(application);

        final ContactDao dao = DbUtil.contactDao(getApplication());
        mContacts = dao.getContacts();

        final GroupDao dao2 = DbUtil.groupDao(getApplication());
        mGroups = dao2.getGroups();
    }

    public LiveData<List<Contact>> getContacts() {
        return mContacts;
    }

    public LiveData<List<Groups>> getGroups() {
        return mGroups;
    }

    void deleteContact(Contact contact) {
        final ContactDao dao = DbUtil.contactDao(getApplication());
        dao.delete(contact);
    }

    void deleteGroup(Groups groups) {
        final GroupDao dao = DbUtil.groupDao(getApplication());
        dao.delete(groups);
    }

    void saveGroup(Groups groups) {
        final GroupDao dao = DbUtil.groupDao(getApplication());
        dao.update(groups);
    }

}
