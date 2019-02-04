package com.example.adne.thenamequizapp.utilities;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {



    public Uri saveImageFromBitmap(Bitmap bitmap, File imageFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return Uri.fromFile(imageFile);
    }



    //Singleton
    private FileUtil() {}
    private static final FileUtil instance = new FileUtil();
    public static FileUtil getInstance() {
        return instance;
    }



}
