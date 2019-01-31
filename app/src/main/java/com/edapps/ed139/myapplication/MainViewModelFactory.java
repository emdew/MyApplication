package com.edapps.ed139.myapplication;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.edapps.ed139.myapplication.database.AppDatabase;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String mCategoryName;

    public MainViewModelFactory(AppDatabase database, String categoryName) {
        mDb = database;
        mCategoryName = categoryName;
    }

    // Note: This can be reused with minor modifications
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ReceiptsViewModel(mDb, mCategoryName);
    }
}
