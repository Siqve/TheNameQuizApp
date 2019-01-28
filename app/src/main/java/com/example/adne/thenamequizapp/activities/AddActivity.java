package com.example.adne.thenamequizapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adne.thenamequizapp.QuizApplication;
import com.example.adne.thenamequizapp.R;
import com.example.adne.thenamequizapp.data.Person;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private QuizApplication quizApplication;

    private Person person;
    private int personIndex = -1;

    private Uri tempImage;

    private ImageView personImage;
    private EditText personName;

    private static final int CAMERA = 0;
    private static final int SELECT_GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        quizApplication = ((QuizApplication) getApplication());

        personImage = findViewById(R.id.imageViewPicture);
        personImage.setOnClickListener(new AddImageClick(this));

        personName = findViewById(R.id.editTextName);

        if (getIntent().hasExtra("personindex")) {
            person = quizApplication.getPersonList().get(personIndex = getIntent().getIntExtra("personindex", 0));
            fillData();
        }
    }

    /**
     * Fills the views with data if we are editing an already existing person
     */
    private void fillData() {
        ((EditText) findViewById(R.id.editTextName)).setText(person.getName());
        if (person.hasImage())
            updateImage(person.getImage());
    }

    /**
     * Updates the imageview
     */
    private void updateImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            personImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Save button clicked. Saves the person and finishes activity.
     */
    public void onSaveEntry(View v) {
        if (personName.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
            return;
        }
        if (person == null) {
            person = new Person(personName.getText().toString());
            ((QuizApplication) getApplication()).getPersonList().add(person);
        } else {
            person.setName(personName.getText().toString());
        }
        person.setImage(tempImage);

        finish();
    }

    /**
     * Delete button clicked. Deletes the person and finishes activity.
     */
    public void onDeleteEntry(View v) {
        if (personIndex != -1)
            quizApplication.getPersonList().remove(personIndex);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            if (resultCode == Activity.RESULT_OK)
                updateImage(tempImage);
            Log.i("bob123camera", tempImage.getPath());
        } else if (requestCode == SELECT_GALLERY) {
            Log.i("bob123",  "a: " + data.getData().toString());
            if (resultCode == Activity.RESULT_OK) {
                Log.i("bob123",  "asd");
                DateFormat df = new SimpleDateFormat("ddMMyyyy_HH_mm_ss");
                String pictureName = "picture_" + df.format(Calendar.getInstance().getTime());
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = new File(storageDir, pictureName + ".jpg");
                try {
                    image.createNewFile();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    FileOutputStream fos = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();


                    updateImage(tempImage = Uri.fromFile(image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class AddImageClick implements View.OnClickListener {

        private AddActivity calledFrom;

        AddImageClick(AddActivity calledFrom) {
            this.calledFrom = calledFrom;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(calledFrom);
            builder.setTitle(R.string.select_or_take)
                    .setItems(R.array.picture_options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                camera();
                            } else if (which == 1) {
                                selectGallery();
                            }
                        }
                    });
            builder.show();
        }

        private void camera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File imageFile = createImageFile();
            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", imageFile);
            calledFrom.tempImage = photoUri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            calledFrom.startActivityForResult(intent, CAMERA);
        }

        private void selectGallery() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            calledFrom.startActivityForResult(intent, SELECT_GALLERY);
        }


        private File createImageFile() {

            DateFormat df = new SimpleDateFormat("ddMMyyyy_HH_mm_ss");
            String pictureName = "picture_" + df.format(Calendar.getInstance().getTime());
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;
            try {
                image = File.createTempFile(
                        pictureName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

    }

}
