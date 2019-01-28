package com.example.adne.thenamequizapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.adne.thenamequizapp.QuizApplication;
import com.example.adne.thenamequizapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onQuizClick(View view) {
        Intent dbIntent = new Intent(this, QuizActivity.class);
        startActivity(dbIntent);
    }

    public void onDatabaseClick(View v) {
        Intent dbIntent = new Intent(this, DatabaseActivity.class);
        startActivity(dbIntent);
    }

}
