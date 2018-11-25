package com.community.jboss.leadmanagement.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.community.jboss.leadmanagement.data.converters.ContactsConverter;

import java.util.List;
import java.util.UUID;

@Entity
public class Groups {
    @PrimaryKey
    @NonNull
    private final String id;
    private String name;
    private byte[] photo;
    @TypeConverters({ContactsConverter.class})
    private List<String> contacts;

    @Ignore
    public Groups(String name, byte[] photo, List<String> contacts) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.photo = photo;
        this.contacts = contacts;
    }

    public Groups(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

}
