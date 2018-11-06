package com.community.jboss.leadmanagement.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by carbonyl on 09/12/2017.
 */
@Entity
public class Contact {
    @PrimaryKey
    @NonNull
    private final String id;
    private String name;
    private String email;
    private byte[] photo;

    @Ignore
    public Contact(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public Contact(@NonNull String id, String name) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
