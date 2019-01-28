package com.example.adne.thenamequizapp.data;

import android.net.Uri;

public class Person {

    private String name;
    private Uri image;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, Uri image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public boolean hasImage() {
        return image != null;
    }
}
