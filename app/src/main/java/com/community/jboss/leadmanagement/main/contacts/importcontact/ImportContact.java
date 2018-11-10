package com.community.jboss.leadmanagement.main.contacts.importcontact;

public class ImportContact {

    private String name;
    private String number;
    private byte[] photo;
    private boolean checked = false;

    ImportContact(String name, String number, byte[] photo) {
        this.name = name;
        this.number = number;
        this.photo = photo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}

