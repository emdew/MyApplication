package com.edapps.ed139.myapplication.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "ReceiptCategories")
public class CategoryModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;

    @Ignore
    private List<ReceiptEntity> receipts;

    @Ignore
    public CategoryModel(String category){
        this.category = category;
    }

    public CategoryModel(int id, String category){
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
