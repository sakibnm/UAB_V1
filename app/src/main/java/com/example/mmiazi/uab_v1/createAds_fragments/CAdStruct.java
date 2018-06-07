package com.example.mmiazi.uab_v1.createAds_fragments;

public class CAdStruct {
    String name;
    float rating;
    String productName;
    String comment;
    String userPhoto;
    String productPhoto;
    boolean nameIsChecked;
    boolean ratingIsChecked;
    boolean commentIsChecked;
    boolean userPhotoIsChecked;

    public CAdStruct(String name, float rating, String productName, String comment, String userPhoto, String productPhoto) {
        this.name = name;
        this.rating = rating;
        this.productName = productName;
        this.comment = comment;
        this.userPhoto = userPhoto;
        this.productPhoto = productPhoto;
    }

    @Override
    public String toString() {
        return "CAdStruct{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", productName='" + productName + '\'' +
                ", comment='" + comment + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", productPhoto='" + productPhoto + '\'' +
                ", nameIsChecked=" + nameIsChecked +
                ", ratingIsChecked=" + ratingIsChecked +
                ", commentIsChecked=" + commentIsChecked +
                ", userPhotoIsChecked=" + userPhotoIsChecked +
                '}';
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

    public void setRating(float rating) {
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

    public boolean isNameIsChecked() {
        return nameIsChecked;
    }

    public void setNameIsChecked(boolean nameIsChecked) {
        this.nameIsChecked = nameIsChecked;
    }

    public boolean isRatingIsChecked() {
        return ratingIsChecked;
    }

    public void setRatingIsChecked(boolean ratingIsChecked) {
        this.ratingIsChecked = ratingIsChecked;
    }

    public boolean isCommentIsChecked() {
        return commentIsChecked;
    }

    public void setCommentIsChecked(boolean commentIsChecked) {
        this.commentIsChecked = commentIsChecked;
    }

    public boolean isUserPhotoIsChecked() {
        return userPhotoIsChecked;
    }

    public void setUserPhotoIsChecked(boolean userPhotoIsChecked) {
        this.userPhotoIsChecked = userPhotoIsChecked;
    }
}
