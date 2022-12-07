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
    private int postLikeCount;
    private int postCommentCount;

    public HomePostModel(String postId, String postUserId, String postTitle, String postImg) {
        this.postId = postId;
        this.postUserId = postUserId;
        this.postTitle = postTitle;
        this.postImg = postImg;
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

    public int getPostLikeCount() {
        return postLikeCount;
    }

    public void setPostLikeCount(int postLikeCount) {
        this.postLikeCount = postLikeCount;
    }

    public int getPostCommentCount() {
        return postCommentCount;
    }

    public void setPostCommentCount(int postCommentCount) {
        this.postCommentCount = postCommentCount;
    }
}
