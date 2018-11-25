package com.community.jboss.leadmanagement.main.groups.editgroup;

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

public class EditGroupActivityViewModel extends AndroidViewModel {

    public EditGroupActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Contact>> getContacts() {
        final ContactDao dao = DbUtil.contactDao(getApplication().getApplicationContext());
        return dao.getContacts();
    }

    void saveGroup(boolean isNew, Groups groups) {
        final GroupDao dao = DbUtil.groupDao(getApplication());
        if (isNew) dao.insert(groups);
        else dao.update(groups);
    }

    Groups getGroup(String id) {
        return DbUtil.groupDao(getApplication()).getGroup(id);
    }

}
