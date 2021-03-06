package com.example.mapp_assignment.models;


import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {
//    private String userName;
//    private String email;
//    private String imageURL;
//    private String userId;
//    private int groupCount;
//    private int eventCount;

    private String grpID;
    private String senderId;
    private String grpName;
    private String imageurl;
    private String lastMsg;
    private Date timestamp;


    public Chat(){

    }


    public Chat(String grpID, String senderId, String grpName, String imageurl, String lastMsg, Timestamp timestamp) {
        this.grpID = grpID;
        this.senderId = senderId;
        this.grpName = grpName;
        this.imageurl = imageurl;
        this.lastMsg = lastMsg;
        this.timestamp = timestamp;

    }

    public String getGrpID() { return grpID; }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
    }
}
