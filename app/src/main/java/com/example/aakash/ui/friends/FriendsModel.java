package com.example.aakash.ui.friends;

import androidx.annotation.Keep;

@Keep
public class FriendsModel {
    private String friendId, friendIconUrl, friendUsername, friendMembers;

    public FriendsModel(String friendId, String friendIconUrl, String friendUsername, String friendMember) {
        this.friendId = friendId;
        this.friendIconUrl = friendIconUrl;
        this.friendUsername = friendUsername;
        this.friendMembers = friendMember;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendIconUrl() {
        return friendIconUrl;
    }

    public void setFriendIconUrl(String friendIconUrl) {
        this.friendIconUrl = friendIconUrl;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendMembers() {
        return friendMembers;
    }

    public void setFriendMembers(String friendMember) {
        this.friendMembers = friendMember;
    }
}
