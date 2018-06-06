package com.example.mmiazi.uab_v1;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
    private String gender;
    private String phone;
    private String address;
    private String imageDownloadUrl;

    public String getImageDownloadUrl() {
        return imageDownloadUrl;
    }

    public void setImageDownloadUrl(String imageDownloadUrl) {
        this.imageDownloadUrl = imageDownloadUrl;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    User(String firstName, String lastName, String email, String password, String repeatPassword, String gender, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.gender = gender;

        this.phone = phone;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", imageDownloadUrl='" + imageDownloadUrl + '\'' +
                '}';
    }
}
