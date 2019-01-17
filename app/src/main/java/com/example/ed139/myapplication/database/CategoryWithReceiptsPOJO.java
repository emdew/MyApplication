package com.example.ed139.myapplication.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class CategoryWithReceiptsPOJO {

    @Embedded
    public CategoryModel categoryModel;

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = ReceiptEntity.class)
    public List<ReceiptEntity> receiptsList;


    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public List<ReceiptEntity> getReceiptsList() {
        return receiptsList;
    }
}
