package com.jadhav.aakash.supports;

import androidx.annotation.Keep;

@Keep
public class User {
    private String username;
    private String email;
    private String profileImg;
    private int memberCount;
    private String datetime;

    public User() {
    }

    public User(String username, String email, String profileImg, int memberCount, String datetime) {
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.memberCount = memberCount;
        this.datetime = datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
