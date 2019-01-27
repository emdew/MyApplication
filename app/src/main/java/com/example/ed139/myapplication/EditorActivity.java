package com.example.ed139.myapplication;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.database.CategoryModel;
import com.example.ed139.myapplication.database.ReceiptEntity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "EditorActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Extra for the receipt ID to be received in the intent
    public static final String EXTRA_RECEIPT_ID = "extraTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_RECEIPT_ID = -1;
    private int mReceiptId = DEFAULT_RECEIPT_ID;
    ListView listView;

    // string adapter listview
    ArrayAdapter<String> adapter;

    // arraylist for listview
    List<String> userCreatedList = new ArrayList<>();

    // stuff for database
    LiveData<List<CategoryModel>> mCategoriesList;
    AppDatabase mDb;
    EditText mPriceET;
    EditText mLocationET;
    View view;

    private ImageView mImageView;
    String mCurrentPhotoPath;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDb = AppDatabase.getInstance(getApplicationContext());
        listView = findViewById(R.id.user_created_list);
        mPriceET = findViewById(R.id.price_et);
        mLocationET = findViewById(R.id.location_et);
        mImageView = (ImageView) findViewById(R.id.image_view_editor);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // load categories into listview
                userCreatedList = mDb.categoryDao().getCategories();
                adapter = new ArrayAdapter<String>(EditorActivity.this, android.R.layout.simple_list_item_multiple_choice, userCreatedList);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        });

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra(String.valueOf("Flag"));
        if (intent.hasExtra(EXTRA_RECEIPT_ID)) {
            if (mReceiptId == DEFAULT_RECEIPT_ID) {
                mReceiptId = intent.getIntExtra(EXTRA_RECEIPT_ID, DEFAULT_RECEIPT_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final ReceiptEntity entity = mDb.receiptDao().loadReceiptById(mReceiptId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUi(entity);
                            }
                        });
                    }
                });
            }
        } else if (checkFlag != null && checkFlag.equals("widget")){
            takePicture(view);
        } else if (checkFlag != null && checkFlag.equals("main_activity")){
            // do nothing
        }
    }

    private void populateUi(ReceiptEntity entity) {
        if (entity == null) {
            return;
        }
        File file = new File(entity.getImage());
       // mImageView.setImageURI(Uri.parse(file));
        Picasso.get().load(file).into(mImageView);
        mPriceET.setText("" + entity.getPrice());
        mLocationET.setText(entity.getLocation());
    }

    // Picture taking capability with button
    // hoping to make this full size
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                // logging
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.ed139.myapplication", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Once image is taken, save to image view and then save to db as a ByteArray
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap(bitmap);
            //File imgFile = new  File(pictureImagePath);
            //if(imgFile.exists()){
//                Bundle extras = data.getExtras();
//                //photos = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                photos = (Bitmap) extras.get("data");
//                mImageView.setImageBitmap(photos);
            //}
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // save the receipt entry to database
    // onClick
    public void saveReceipt(View view) {
        // checked id and get name from checked id
        int categoryId = listView.getCheckedItemPosition();
        String categoryName = userCreatedList.get(categoryId);

        String location = mLocationET.getText().toString();
        Double price = Double.parseDouble(mPriceET.getText().toString());

        // save receipt
        final ReceiptEntity receiptEntity = new ReceiptEntity(mCurrentPhotoPath, price, location, categoryName);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // insert into DB
                if (mReceiptId == DEFAULT_RECEIPT_ID) {
                    mDb.receiptDao().insertReceipt(receiptEntity);
                } else {
                    receiptEntity.setId(mReceiptId);
                    mDb.receiptDao().updateReceipt(receiptEntity);
                }
                // sent to MainActivity to ensure that the TodayFragment refreshes immediately
                setResult(Activity.RESULT_OK);
                // bail
                finish();
            }
        });
    }
}
