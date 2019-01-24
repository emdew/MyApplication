package com.example.ed139.myapplication;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.database.CategoryModel;
import com.example.ed139.myapplication.database.ReceiptEntity;

import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "EditorActivity";
    // Extra for the receipt ID to be received in the intent
    public static final String EXTRA_RECEIPT_ID = "extraTaskId";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDb = AppDatabase.getInstance(getApplicationContext());
        listView = findViewById(R.id.user_created_list);
        mPriceET = findViewById(R.id.price_et);
        mLocationET = findViewById(R.id.location_et);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // load categories into listview
                userCreatedList = mDb.categoryDao().getCategories();
                // get same info in order to get the id for receipt item
                mCategoriesList = mDb.categoryDao().loadCategories();
                adapter = new ArrayAdapter<String>(EditorActivity.this, android.R.layout.simple_list_item_multiple_choice, userCreatedList);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        });
    }

    // save the receipt entry to database
    // onClick
    public void saveReceipt(View view) {

        // checked id and name from checked id
        int categoryId = listView.getCheckedItemPosition();
        String categoryName = userCreatedList.get(categoryId);

        String location = mLocationET.getText().toString();
        Long price = Long.parseLong(mPriceET.getText().toString());

        // save receipt
        final ReceiptEntity receiptEntity = new ReceiptEntity(price, location, categoryName);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.receiptDao().insertReceipt(receiptEntity);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
