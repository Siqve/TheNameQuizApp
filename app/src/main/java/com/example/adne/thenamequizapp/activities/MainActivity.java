package com.example.adne.thenamequizapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.adne.thenamequizapp.R;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    private Menu ownerNameMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.ownerNameMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        checkForOwner(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            openOwnerNameDialog(getOwnerName());
        }
        return super.onOptionsItemSelected(item);
    }


    private void checkForOwner(Menu menu) {
        String ownerName = getOwnerName();
        if (ownerName != null)
            menu.getItem(0).setTitle(ownerName);
        else
            openOwnerNameDialog(null);
    }

    private String getOwnerName() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        return preferences.getString("owner", null);
    }

    private void setOwnerName(String ownerName) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        preferences.edit().putString("owner", ownerName).apply();
        ownerNameMenu.getItem(0).setTitle(ownerName);
    }

    private void openOwnerNameDialog(final String oldOwnerName) {
        final AlertDialog.Builder ownerNameDialog = new AlertDialog.Builder(this);
        ownerNameDialog.setTitle(R.string.owner_name);

        final EditText ownerNameInput = new EditText(this);
        ownerNameInput.setText(oldOwnerName != null ? oldOwnerName : " ");
        ownerNameInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        ownerNameDialog.setView(ownerNameInput);
        ownerNameDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ownerNameInput.getText().length() == 0)
                    openOwnerNameDialog(oldOwnerName);
                else
                    setOwnerName(ownerNameInput.getText().toString());
            }
        });

        if (oldOwnerName != null) {
            ownerNameDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        ownerNameDialog.show();
    }

    /**
     * Quiz button click
     */
    public void onQuizClick(View view) {
        Intent dbIntent = new Intent(this, QuizActivity.class);
        startActivity(dbIntent);
    }

    /**
     * Database button click
     */
    public void onDatabaseClick(View v) {
        Intent dbIntent = new Intent(this, DatabaseActivity.class);
        startActivity(dbIntent);
    }

}
