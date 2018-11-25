package com.community.jboss.leadmanagement.data.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ContactsConverter {

    @TypeConverter
    public List<String> storedStringToContacts(String value) {
        return new Gson().fromJson(value, new TypeToken<List<String>>() {
        }.getType());
    }

    @TypeConverter
    public String contactsToStoredString(List<String> contacts) {
        return new Gson().toJson(contacts);
    }

}
