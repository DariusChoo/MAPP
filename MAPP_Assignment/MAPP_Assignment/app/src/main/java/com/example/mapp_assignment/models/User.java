package com.example.mapp_assignment.models;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

    public class User {


        private String userName;
        private String email;
        private String imageURL;
        private String userId;
        private int groupCount;
        private int eventCount;
        private ArrayList<String> groupsId;
        private ArrayList<String> eventsId;

        public User(){

        }


        public User(String userName, String email, String imageURL, String userId, int groupCount, int eventCount ,ArrayList<String> groupsId, ArrayList<String> eventsId) {
            this.userName = userName;
            this.email = email;
            this.imageURL = imageURL;
            this.userId = userId;
            this.groupCount = groupCount;
            this.eventCount = eventCount;
            this.groupsId = groupsId;
            this.eventsId = eventsId;
        }

        public ArrayList<String> getGroupsId() {
            return groupsId;
        }

        public void setGroupsId(ArrayList<String> groupsId) {
            this.groupsId = groupsId;
        }

        public ArrayList<String> getEventsId() {
            return eventsId;
        }

        public void setEventsId(ArrayList<String> eventsId) {
            this.eventsId = eventsId;
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

