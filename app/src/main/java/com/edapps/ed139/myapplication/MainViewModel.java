package com.edapps.ed139.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.edapps.ed139.myapplication.database.AppDatabase;
import com.edapps.ed139.myapplication.database.ReceiptEntity;
import com.edapps.ed139.myapplication.database.CategoryModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<CategoryModel>> categories;
    private LiveData<List<ReceiptEntity>> receipts;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        categories = appDatabase.categoryDao().loadCategories();
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return categories;
    }

    public LiveData<List<ReceiptEntity>> getReceipts() {
        return receipts;
    }
}
