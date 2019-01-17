package com.example.ed139.myapplication.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM ReceiptCategories ORDER BY id")
    LiveData<List<CategoryModel>> loadCategories();

    @Transaction @Query("SELECT * FROM ReceiptCategories")
    LiveData<List<CategoryWithReceiptsPOJO>> loadCategoriesWithReceipts();

    @Query("SELECT category FROM ReceiptCategories ORDER BY id")
    List<String> getCategories();

    @Query("DELETE FROM ReceiptCategories")
    void delete();

//    @Query("SELECT SUM(price) as total FROM Category")
//    Long getSumOfPrices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryModel categoryModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCategory(CategoryModel categoryModel);

    @Delete
    void deleteCategory(CategoryModel categoryModel);
}