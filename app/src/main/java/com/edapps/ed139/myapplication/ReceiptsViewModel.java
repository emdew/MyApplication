package com.edapps.ed139.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.edapps.ed139.myapplication.database.AppDatabase;
import com.edapps.ed139.myapplication.database.ReceiptEntity;

import java.util.List;

public class ReceiptsViewModel extends ViewModel {

    private LiveData<List<ReceiptEntity>> receipt;

    public ReceiptsViewModel(AppDatabase database, String categoryName) {
        receipt = database.receiptDao().findReceiptsForCategory(categoryName);
    }

    public LiveData<List<ReceiptEntity>> getReceipt() {
        return receipt;
    }
}
