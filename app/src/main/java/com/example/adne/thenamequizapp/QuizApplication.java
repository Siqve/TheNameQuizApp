package com.example.adne.thenamequizapp;

import android.app.Application;
import android.net.Uri;

import com.example.adne.thenamequizapp.data.Person;

import java.util.ArrayList;
import java.util.List;

public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Uri uri = Uri.parse("android.resource://com.example.adne.thenamequizapp/drawable/jone");
        persons.add(new Person("Jone", uri));

    }

    private List<Person> persons = new ArrayList<>();

    public List<Person> getPersonList() {
        return persons;
    }
}
