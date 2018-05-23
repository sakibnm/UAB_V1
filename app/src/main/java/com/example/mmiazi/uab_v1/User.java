package com.example.mmiazi.uab_v1;

import android.graphics.Bitmap;
import android.net.Uri;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
    private String phone;
    private String address;
    private Bitmap userPhoto;
    private Uri imageDownloadUri;

    public Uri getImageDownloadUri() {
        return imageDownloadUri;
    }

    public void setImageDownloadUri(Uri imageDownloadUri) {
        this.imageDownloadUri = imageDownloadUri;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    User(String firstName, String lastName, String email, String password, String repeatPassword, String phone, String address, Bitmap userPhoto){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.phone = phone;
        this.address = address;
        this.userPhoto = userPhoto;
    }


}
