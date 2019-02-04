package com.example.adne.thenamequizapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.adne.thenamequizapp.R;
import com.example.adne.thenamequizapp.data.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseUtil {

    private static final String SPLITTER = "-/-";

    private HashSet<String> localPersonDatabase = new HashSet<>();

    private FirebaseStorage storage;

    private DatabaseUtil() {
        storage = FirebaseStorage.getInstance();
    }


    public void addPerson(final Context context, final Uri uri, final String name) {
        StorageReference storageImageRef = storage.getReference().child("images/" + uri.getLastPathSegment());
        UploadTask uploadTask = storageImageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("FirebaseImageUploader", "Failed to upload image from URI: " + uri.getPath());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                addLocalPerson(context, uri.getLastPathSegment(), name);
            }
        });
    }

    private void addLocalPerson(Context context, String fileName, String personName) {
        localPersonDatabase.add(fileName + SPLITTER + personName);
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        preferences.edit().putStringSet("persondatabase", localPersonDatabase).apply();
    }

    public void addPersonsFromLocal(Context context, Set<String> localPersonDatabase, final List<Person> persons) {
        this.localPersonDatabase = new HashSet<>(localPersonDatabase);
        for (String person : localPersonDatabase) {
            String[] split  = person.split(SPLITTER);
            final String imageName = split[0];
            final String personName = split[1];

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference firebaseImage = storage.getReference().child("images/" + imageName);

            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File localFile = new File(storageDir, firebaseImage.getName());

            if (localFile.exists()) {
                persons.add(new Person(personName, Uri.fromFile(localFile)));
            } else {
                firebaseImage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        persons.add(new Person(personName, Uri.fromFile(localFile)));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("FirebaseImageDownloader", "Failed to download image from person \""
                                + personName + "\", filename \"" + imageName + "\"");
                    }
                });
            }
        }
    }


    //Singleton
    private static final DatabaseUtil instance = new DatabaseUtil();
    public static DatabaseUtil getInstance() {
        return instance;
    }



}
