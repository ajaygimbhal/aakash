package com.jadhav.aakash.ui.home;

import androidx.annotation.Keep;

@Keep
public class HomePostModel {

    private String postId;
    private String postUserId;
    private String postUsername;
    private String postUserIcon;
    private String postTitle;
    private String postImg;
    private String postLikeCount;
    private String postCommentCount;

    public HomePostModel(String postId, String postUserId, String postUsername, String postUserIcon, String postTitle, String postImg, String postLikeCount, String postCommentCount) {
        this.postId = postId;
        this.postUserId = postUserId;
        this.postUsername = postUsername;
        this.postUserIcon = postUserIcon;
        this.postTitle = postTitle;
        this.postImg = postImg;
        this.postLikeCount = postLikeCount;
        this.postCommentCount = postCommentCount;
    }

    public String getPostUserIcon() {
        return postUserIcon;
    }

    public void setPostUserIcon(String postUserIcon) {
        this.postUserIcon = postUserIcon;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostLikeCount() {
        return postLikeCount;
    }

    public void setPostLikeCount(String postLikeCount) {
        this.postLikeCount = postLikeCount;
    }

    public String getPostCommentCount() {
        return postCommentCount;
    }

    public void setPostCommentCount(String postCommentCount) {
        this.postCommentCount = postCommentCount;
    }
}
