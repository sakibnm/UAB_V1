package com.example.mmiazi.uab_v1.receivedAds_fragments;

public class AdStruct {

    String name;
    float rating;
    String productName;
    String comment;
    String userPhoto;
    String productPhoto;

    AdStruct() {

    }

    @Override
    public String toString() {
        return "name = " + name +
                " rating = " + rating +
                " productName = " + productName +
                " comment = " + comment +
                " userPhoto = " + userPhoto +
                " productPhoto = " + productPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    AdStruct(String name, int rating, String productName, String comment, String userPhoto, String productPhoto) {
        this.name = name;
        this.rating = rating;
        this.productName = productName;
        this.comment = comment;
        this.userPhoto = userPhoto;
        this.productPhoto = productPhoto;

    }

}
