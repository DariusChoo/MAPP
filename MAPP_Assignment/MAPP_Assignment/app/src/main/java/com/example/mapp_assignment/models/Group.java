package com.example.mapp_assignment.models;


import java.util.ArrayList;

public class Group {
    private String groupName;
    private String groupDescription;
    private String imageURL;
    private String creatorId;
    private String category;
    private String interest;
    private int groupMemberCount;
    private ArrayList<String> membersId;

    public Group() {
    }


    public Group(String groupName, String groupDescription, String imageURL, String creatorId, String category, String interest, int groupMemberCount, ArrayList<String>membersId) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.imageURL = imageURL;
        this.creatorId = creatorId;
        this.category = category;
        this.interest = interest;
        this.groupMemberCount = groupMemberCount;
        this.membersId = membersId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }

    public void setGroupMemberCount(int groupMemberCount) {
        this.groupMemberCount = groupMemberCount;
    }

    public ArrayList<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(ArrayList<String> membersId) {
        this.membersId = membersId;
    }

}
