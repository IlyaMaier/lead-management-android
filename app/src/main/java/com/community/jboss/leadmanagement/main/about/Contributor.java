package com.community.jboss.leadmanagement.main.about;

import com.google.gson.annotations.SerializedName;

public class Contributor {

    @SerializedName("login")
    private String name;
    @SerializedName("avatar_url")
    private String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}