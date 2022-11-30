package com.jadhav.aakash.ui.friends;

import androidx.annotation.Keep;

@Keep
public class FriendsModel {
    private String friendId, friendIconUrl, friendUsername;
    private int friendMemberCount;

    public FriendsModel(String friendId, String friendIconUrl, String friendUsername, int friendMemberCount) {
        this.friendId = friendId;
        this.friendIconUrl = friendIconUrl;
        this.friendUsername = friendUsername;
        this.friendMemberCount = friendMemberCount;
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

    public int getFriendMemberCount() {
        return friendMemberCount;
    }

    public void setFriendMemberCount(int friendMemberCount) {
        this.friendMemberCount = friendMemberCount;
    }
}
