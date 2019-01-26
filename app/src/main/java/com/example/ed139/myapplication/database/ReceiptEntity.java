package com.example.ed139.myapplication.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Receipts")
public class ReceiptEntity {

    // receipt id
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String image;

    // category id
    public String categoryName;

    // price
    private Long price;

    // location
    private String location;

    @Ignore
    public ReceiptEntity(String image, Long price, String location, String categoryName) {
        this.image = image;
        this.price = price;
        this.location = location;
        this.categoryName = categoryName;
    }

    public ReceiptEntity(int id, String image, Long price, String location, String categoryName) {
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
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

    //    public int getYear(){
//        return year;
//    }
//
//    public void setYear(int year){
//        this.year = year;
//    }
//
//    public int getMonth(){
//        return month;
//    }
//
//    public void setMonth(int month){
//        this.month = month;
//    }
//
//    public int getDay(){
//        return day;
//    }
//
//    public void setDay(int day){
//        this.day = day;
//    }
//
//    public int getHour(){
//        return hour;
//    }
//
//    public void setHour(int hour){
//        this.hour = hour;
//    }
//
//    public int getMinute(){
//        return minute;
//    }
//
//    public void setMinute(int minute){
//        this.minute = minute;
//    }
}
