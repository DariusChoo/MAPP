package com.example.mapp_assignment.models;

import com.google.firebase.firestore.CollectionReference;

public class User {


    private String userName;
    private String email;
    private String imageURL;
    private String userId;
    private int groupCount;
    private int eventCount;

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

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }
}
