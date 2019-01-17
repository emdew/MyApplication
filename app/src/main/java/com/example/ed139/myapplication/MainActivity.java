package com.example.ed139.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // database instance for reset button below
        mDb = AppDatabase.getInstance((this.getApplicationContext()));

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // toggle for the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // should automatically begin app on Today fragment
        // if saved instance state is null
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TodayFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_today);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PackageManager pm = MainActivity.this.getPackageManager();
//                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Sorry! You don't have a camera app.", Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_today:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TodayFragment()).commit();
                break;
            case R.id.nav_reset:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.categoryDao().delete();
                        mDb.receiptDao().delete();
                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TodayFragment()).commit();
                Toast.makeText(this, "Reset all data", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
