package com.edapps.ed139.myapplication.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<CategoryModel>> categories;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        categories = appDatabase.categoryDao().loadCategories();
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return categories;
    }
}
