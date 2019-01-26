package com.example.ed139.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Emily on 11/9/17.
 */
public class Utils {

    // Adapted Utils file from example app online
    // http://www.coderzheaven.com/2012/12/23/store-image-android-sqlite-retrieve-it/

    // Used in saveItem
    // Adding the if/else statement here allows the picture to be null
    // But now the picture disappears when you update an item without taking a new pic.
    public static byte[] getImageBytes(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    // Used in OnLoadFinished (Editor) and cursor adapter
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
