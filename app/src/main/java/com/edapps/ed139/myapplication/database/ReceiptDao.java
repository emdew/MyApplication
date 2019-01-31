package com.edapps.ed139.myapplication.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReceiptDao {

    @Query("SELECT * FROM Receipts ORDER BY id")
    LiveData<List<ReceiptEntity>> loadReceipts();

    @Query("SELECT * FROM Receipts WHERE categoryName IS NULL")
    ReceiptEntity findRootCategory();

    @Query("SELECT * FROM Receipts WHERE categoryName=:categoryName")
    List<ReceiptEntity> findReceiptsForCategory(final String categoryName);

    @Query("DELETE FROM Receipts")
    void delete();

    @Query("SELECT * FROM Receipts WHERE id = :id")
    ReceiptEntity loadReceiptById(int id);

    @Query("SELECT SUM(price) FROM Receipts")
    Double getTotal();

    @Insert
    void insertReceipt(ReceiptEntity receiptEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReceipt(ReceiptEntity receiptEntity);

    @Delete
    void deleteReceipt(ReceiptEntity receiptEntity);
}
