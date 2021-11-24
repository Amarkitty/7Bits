package com.example.a7bitstask.user;

public class UserEntityData {
    private String userID = "";
    private String name = "";
    private String mobile = "";
    private String emailID = "";
    private String image = "";

    public UserEntityData() {
    }

    public UserEntityData(String userID, String name, String mobile, String emailID, String image) {
        this.userID = userID;
        this.name = name;
        this.mobile = mobile;
        this.emailID = emailID;
        this.image = image;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
