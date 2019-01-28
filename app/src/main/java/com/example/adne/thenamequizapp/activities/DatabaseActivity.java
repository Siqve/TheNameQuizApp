package com.example.adne.thenamequizapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adne.thenamequizapp.QuizApplication;
import com.example.adne.thenamequizapp.R;
import com.example.adne.thenamequizapp.data.Person;
import com.example.adne.thenamequizapp.data.PersonArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    private List<Person> persons;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        persons = ((QuizApplication) getApplication()).getPersonList();

        setupPersonList();
        setupFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPersonList();
    }

    private void setupPersonList() {
        getListView().setAdapter(new PersonArrayAdapter(this, android.R.layout.simple_list_item_1,
                persons));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent addIntent = new Intent(view.getContext(), AddActivity.class);
                addIntent.putExtra("personindex", position);
                startActivity(addIntent);
            }
        });

    }



    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(addIntent);
            }
        });
    }


    private ListView getListView() {
        if (mListView == null)
            mListView = (ListView) findViewById(R.id.databaseList);
        return mListView;
    }

}
