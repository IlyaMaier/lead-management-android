package com.community.jboss.leadmanagement.main.groups;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.community.jboss.leadmanagement.data.daos.GroupDao;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.utils.DbUtil;

import java.util.List;

public class GroupsFragmentViewModel extends AndroidViewModel {

    private LiveData<List<Groups>> groups;

    public GroupsFragmentViewModel(@NonNull Application application) {
        super(application);

        final GroupDao dao = DbUtil.groupDao(getApplication());
        groups = dao.getGroups();
    }

    public LiveData<List<Groups>> getGroups() {
        return groups;
    }

    void deleteGroup(Groups groups){
        final GroupDao dao = DbUtil.groupDao(getApplication());
        dao.delete(groups);
    }

}
