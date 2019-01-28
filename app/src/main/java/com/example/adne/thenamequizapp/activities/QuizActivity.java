package com.example.adne.thenamequizapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adne.thenamequizapp.QuizApplication;
import com.example.adne.thenamequizapp.R;
import com.example.adne.thenamequizapp.data.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ImageView pictureImageView;
    private EditText guessEditText;
    private TextView scoreTextView;
    private TextView highscoreTextView;

    private int highscore;
    private int score;

    private List<Person> persons;
    private int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        pictureImageView = findViewById(R.id.guessPictureImageView);
        guessEditText = findViewById(R.id.guessEditText);
        highscoreTextView = findViewById(R.id.highscoreTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        if (preferences.contains("highscore"))
            highscore = preferences.getInt("highscore", 0);
        checkHighscore();
        updateScore();

        persons = new ArrayList<>(((QuizApplication) getApplication()).getPersonList());
        start();
    }

    /**
     * Starts the quiz
     */
    private void start() {
        if (persons.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_database), Toast.LENGTH_LONG).show();
            return;
        }
        score = 0;
        Collections.shuffle(persons);
        nextPerson();
    }

    /**
     * Shows the next person in the quiz
     */
    private void nextPerson() {
        if (currentIndex >= persons.size()) {
            currentIndex = 0;
            Collections.shuffle(persons);
        }
        Person tempPerson = persons.get(currentIndex);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempPerson.getImage());
            pictureImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TheNameQuizApp", "Failed to load image for person: " + tempPerson.getName());
            currentIndex++;
            nextPerson();
            return;
        }
        currentIndex++;
    }


    public void makeGuess(View view) {
        if (guessEditText.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
            return;
        }
        String guess = guessEditText.getText().toString();
        if (guess.equalsIgnoreCase(persons.get((currentIndex-1) % persons.size()).getName())) {
            score++;
            updateScore();
            checkHighscore();
            Toast.makeText(getApplicationContext(), getString(R.string.guess_correct), Toast.LENGTH_SHORT).show();
        } else {
            wrongGuess();
        }
        nextPerson();
    }

    public void wrongGuess() {
        score = 0;
        updateScore();
        Toast.makeText(getApplicationContext(), getString(R.string.guess_incorrect), Toast.LENGTH_SHORT).show();
    }

    private void updateScore() {
        String scoreText = getText(R.string.score) + ": " + score;
        scoreTextView.setText(scoreText);
    }

    private void checkHighscore() {
        if (score > highscore)
            highscore = score;
        String highscoreText = getText(R.string.highscore) + ": " + highscore;
        highscoreTextView.setText(highscoreText);
    }

}
