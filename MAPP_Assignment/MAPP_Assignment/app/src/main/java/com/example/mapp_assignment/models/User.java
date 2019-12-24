package com.example.mapp_assignment.models;

public class User {


    private String userName;
    private String email;
    private String imageURL;
    private String userId;

    public User(){

    }

    public User(String userName, String email, String imageURL, String userId) {
        this.userName = userName;
        this.email = email;
        this.imageURL = imageURL;
        this.userId = userId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
