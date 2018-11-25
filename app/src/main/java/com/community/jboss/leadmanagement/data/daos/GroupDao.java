package com.community.jboss.leadmanagement.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.community.jboss.leadmanagement.data.entities.Groups;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Groups group);

    @Delete
    void delete(Groups group);

    @Update
    void update(Groups group);

    @Query("SELECT * FROM groups WHERE id = :id")
    Groups getGroup(String id);

    @Query("SELECT * FROM groups")
    LiveData<List<Groups>> getGroups();
}
