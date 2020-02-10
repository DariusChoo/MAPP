package com.example.mapp_assignment.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    private String groupName;
    private String groupId;
    private String eventName;
    private String eventDescription;
    private String eventImageUrl;
    private String location;
    private String eventDate;
    private String eventTime;
    private ArrayList<String> participantsId;

    public Event(){


    }

    public Event(String groupName, String groupId, String eventName, String eventDescription, String eventImageUrl, String location, String eventDate, String eventTime, ArrayList<String> participantsId) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventImageUrl = eventImageUrl;
        this.location = location;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.participantsId = participantsId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public ArrayList<String> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(ArrayList<String> participantsId) {
        this.participantsId = participantsId;
    }
}
