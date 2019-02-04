package com.example.adne.thenamequizapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.adne.thenamequizapp.data.Person;
import com.example.adne.thenamequizapp.utilities.DatabaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        if (!sharedPref.contains("persondatabase"))
            return;
        Set<String> localPersonDatabase = sharedPref.getStringSet("persondatabase", null);
        DatabaseUtil.getInstance().addPersonsFromLocal(getApplicationContext(), localPersonDatabase, persons);
    }

    private List<Person> persons = new ArrayList<>();

    public List<Person> getPersonList() {
        return persons;
    }
}
