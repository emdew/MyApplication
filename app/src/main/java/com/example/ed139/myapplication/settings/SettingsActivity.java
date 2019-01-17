package com.example.ed139.myapplication.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ed139.myapplication.AppExecutors;
import com.example.ed139.myapplication.R;
import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.database.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    // arraylist of items
    List<CategoryModel> categoriesList;

    EditText mEditText;
    StringBuilder userList;
    List<String> userCreatedList = new ArrayList<>();

    AppDatabase mDb;
    ListView settingsList;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("userList", userList.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
//        String userListString = state.getString("userList");
//        String[] items = userListString.split(",");
//        ArrayList<String> userList = new ArrayList<String>();
//        for(int i = 0; i < items.length; i++) userList.add(items[i]);
//        adapter = new CategoryAdapter(this, categoriesList);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_settings);

        settingsList = findViewById(R.id.list_settings);

        // database instance
        mDb = AppDatabase.getInstance(getApplicationContext());

        // EditText to add categories
        mEditText = (EditText) findViewById(R.id.et_no);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userCreatedList = mDb.categoryDao().getCategories();
                ArrayAdapter<String> adapterForUserList = new ArrayAdapter <String>(SettingsActivity.this, android.R.layout.simple_list_item_1, userCreatedList);
                settingsList.setAdapter(adapterForUserList);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().getCategories();
            }
        });
    }

    // dynamic insertion of each new category
    // each entry is saved into the database
    // ignore duplicates?
    // onClick
    public void addItems(View v) {

        // add a new category to the list
        String newCategory = mEditText.getText().toString();
        userCreatedList.add(newCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_1, userCreatedList);
        settingsList.setAdapter(adapter);

        // save new category to the database
        final CategoryModel categoryModel = new CategoryModel(newCategory);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().insertCategory(categoryModel);
            }
        });
    }

    // save just finishes. AddItems actually saves to DB
    public void saveItems(View v) {
        finish();
    }
}
