package com.example.ed139.myapplication.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Receipts")
public class ReceiptEntity {

    // receipt id
    @PrimaryKey(autoGenerate = true)
    private int id;

    // category id
    public String categoryName;

    private String image;

    // price
    private Double price;

    // location
    private String location;

    @Ignore
    public ReceiptEntity(String image, Double price, String location, String categoryName) {
        this.image = image;
        this.price = price;
        this.location = location;
        this.categoryName = categoryName;
    }

    public ReceiptEntity(int id, String image, Double price, String location, String categoryName) {
        this.image = image;
        this.id = id;
        this.price = price;
        this.location = location;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
